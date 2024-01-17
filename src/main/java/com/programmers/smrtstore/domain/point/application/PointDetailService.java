package com.programmers.smrtstore.domain.point.application;


import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.orderManagement.order.application.OrderService;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.point.application.dto.req.AcmPointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.ReviewPointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.UseCancelPointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.PurchaseRewardPointResponse;
import com.programmers.smrtstore.domain.point.application.facade.PointFacade;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailCustomResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointDetailService {

    private final PointFacade pointFacade;
    private final OrderService orderService;
    private final UserJpaRepository userJpaRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    public Integer saveAccumulationHistory(AcmPointDetailRequest request) {

        validateUserExists(request.getUserId());

        String orderId = request.getOrderId();
        List<PointResponse> acmHistory = pointFacade.getByOrderIdAndStatus(
            orderId, PointStatus.ACCUMULATED
        );

        // 주문에 대한 상품별 결제금액
        List<OrderedProductResponse> products = orderService.getProductsForOrder(orderId);

        int idx = 0;
        int totalAcmPoint = 0;

        // 주문상품별로 적립 이력을 저장
        for (OrderedProductResponse product : products) {
            totalAcmPoint += saveAcmPointDetailPerProduct(product, acmHistory, idx, request);
            idx += product.getQuantity();
        }
        return totalAcmPoint;
    }

    private Integer saveAcmPointDetailPerProduct(OrderedProductResponse product,
        List<PointResponse> acmHistory, int idx, AcmPointDetailRequest request) {

        int totalAcmPoint = 0;

        // 동일한 주문상품의 수량에 대한 적립 이력을 개당 분리해서 저장
        int count = product.getQuantity();
        while (count != 0) {
            PointResponse point = acmHistory.get(idx);
            int pointValue = point.getPointValue();
            PointDetail pointDetail = request.toEntity(
                point.getId(),
                product.getOrderedProductId(),
                pointValue,
                point.getId()
            );
            count -= 1;
            idx += 1;
            totalAcmPoint += pointValue;
            pointDetailRepository.save(pointDetail);
        }
        return totalAcmPoint;
    }

    public Integer saveReviewAcmHistory(ReviewPointDetailRequest request) {

        Long userId = request.getUserId();
        validateUserExists(userId);

        PointDetail pointDetail = request.toEntity();
        pointDetailRepository.save(pointDetail);
        return pointDetail.getPointAmount();
    }

    public Integer saveUseHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        Long userId = request.getUserId();

        PointResponse point = pointFacade.getPointById(pointId);
        String orderId = point.getOrderId();
        int usedPoint = Math.abs(point.getPointValue());

        // 주문에 대한 상품별 결제금액
        List<OrderedProductResponse> products = orderService.getProductsForOrder(orderId);

        // 상품별 결제금액에 대한 비율
        Map<Long, Integer> pointPieces = getPointPiecesPerOrder(products, usedPoint);

        // 포인트 차감을 위한 사용 가능한 포인트 상세 적립 이력
        List<PointDetailCustomResponse> history = pointFacade.getSumGroupByOriginAcmId(userId);

        int i = 0;
        int remainPiece = 0;
        for (PointDetailCustomResponse available : history) {
            if (i == pointPieces.size()) {
                break;
            }
            int pointValue = available.getPointAmount();
            while (pointValue != 0) {
                Long orderedProductId = products.get(i).getOrderedProductId();
                int piece = remainPiece == 0 ? pointPieces.get(orderedProductId) : remainPiece;
                int pointAmount = calculateDeductedPoint(pointValue, piece);
                PointDetail pointDetail = request.toEntity(
                    orderedProductId,
                    pointFacade.makeNegativeNumber(pointAmount),
                    available.getOriginAcmId()
                );
                pointDetailRepository.save(pointDetail);
                pointValue -= pointAmount;
                remainPiece = piece > pointValue ? piece - pointValue : 0;
                if (remainPiece == 0) {
                    i++;
                }
            }
        }
        return usedPoint;
    }

    private int calculateDeductedPoint(int pointAmount, int usedPoint) {
        return Math.min(usedPoint, pointAmount);
    }

    private Map<Long, Integer> getPointPiecesPerOrder(List<OrderedProductResponse> products, int usePoint) {

        Map<Long, Double> productRatio = getOrderedProductsRatio(products);
        Map<Long, Integer> pointPieces = new HashMap<>();

        // 상품별 비율에 따른 사용할 포인트의 비율 계산
        for (int idx = 0; idx < products.size() - 1; idx++) {
            Long orderedProductId = products.get(idx).getOrderedProductId();
            int price = (int) (usePoint * productRatio.get(orderedProductId));
            pointPieces.put(orderedProductId, price);
            // 마지막 차감포인트는 액수를 맞추기 위해 소거법 적용
            usePoint -= price;
        }
        pointPieces.put(products.get(products.size() - 1).getOrderedProductId(), usePoint);
        return pointPieces;
    }

    public List<PurchaseRewardPointResponse> getPurchaseRewardPoint(List<OrderedProductResponse> products, int usePoint) {

        Map<Long, Double> productRatio = getOrderedProductsRatio(products);
        List<PurchaseRewardPointResponse> rewardPoints = new ArrayList<>();
        for (int idx = 0; idx < productRatio.size() - 1; idx++) {
            Long orderedProductId = products.get(idx).getOrderedProductId();
            int price = (int) (usePoint * productRatio.get(orderedProductId));
            PurchaseRewardPointResponse rewardPoint = PurchaseRewardPointResponse.of(
                orderedProductId, price
            );
            rewardPoints.add(rewardPoint);
            // 마지막 차감포인트는 액수를 맞추기 위해 소거법 적용
            usePoint -= price;
        }
        PurchaseRewardPointResponse rewardPoint = PurchaseRewardPointResponse.of(
            products.get(products.size() - 1).getOrderedProductId(), usePoint
        );
        rewardPoints.add(rewardPoint);
        return rewardPoints;
    }

    private Map<Long, Double> getOrderedProductsRatio(List<OrderedProductResponse> products) {

        Map<Long, Double> ratiosByOrderedProductId = new HashMap<>();

        int totalPrice = products.stream()
            .mapToInt(OrderedProductResponse::getTotalPrice)
            .sum();

        double remain = 1.0;
        for (int idx = 0; idx < products.size() - 1; idx++) {
            OrderedProductResponse product = products.get(idx);
            Integer originPrice = product.getTotalPrice();
            double ratio = originPrice.doubleValue() / totalPrice;

            // 주문상품별 비율은 소수점 둘째자리까지만 계산
            BigDecimal ratioBigDecimal = new BigDecimal(Double.toString(ratio));
            double roundedRatio = ratioBigDecimal.setScale(2, RoundingMode.DOWN).doubleValue();
            ratiosByOrderedProductId.put(product.getOrderedProductId(), roundedRatio);
            remain -= roundedRatio;
        }
        // 마지막 주문상품에 대한 비율은 합 (=1)을 맞추기 위해 소거법 적용
        OrderedProductResponse product = products.get(products.size() - 1);
        ratiosByOrderedProductId.put(product.getOrderedProductId(), remain);
        return ratiosByOrderedProductId;
    }

    public Integer saveUseCancelHistory(UseCancelPointDetailRequest request) {

        validateUserExists(request.getUserId());

        int cancelPoint = 0;
        List<Long> cancelProductIds = request.getOrderedProductIds();

        // 주문 취소해야 하는 주문상품 아이디별로 취소 이력을 저장
        for (Long id : cancelProductIds) {

            // 주문상품 아이디에 대한 차감된 포인트 상세 내역
            List<PointDetailResponse> usedDetailHistory = pointFacade
                .getUsedDetailByPointIdAndOrderedProductId(request.getPointId(), id);

            for (PointDetailResponse usedDetail : usedDetailHistory) {
                int pointValue = usedDetail.getPointValue();
                PointDetail pointDetail = request.toEntity(
                    id,
                    pointValue,
                    usedDetail.getOriginAcmId()
                );
                pointDetailRepository.save(pointDetail);
                cancelPoint += pointValue;

                PointResponse originAcmPoint = pointFacade.getPointById(pointDetail.getOriginAcmId());
                if (pointFacade.validateExpiredAt(originAcmPoint)) {
                    int pointAmount = pointDetail.getPointAmount();
                    PointDetail expiredDetail = request.toEntity(
                        null,
                        pointAmount,
                        pointDetail.getOriginAcmId());
                    pointDetailRepository.save(expiredDetail);
                    cancelPoint -= pointAmount;
                }
            }
        }
        return cancelPoint;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public Integer saveExpirationHistory() {

        List<ExpiredPointDetailResponse> expireHistory = pointFacade.getExpiredSumGroupByOriginAcmId();

        int expiredPoint = 0;
        for (ExpiredPointDetailResponse expireDetail : expireHistory) {
            PointDetail pointDetail = PointDetail.makeExpirationHistory(expireDetail);
            pointDetailRepository.save(pointDetail);
            expiredPoint += pointDetail.getPointAmount();
        }
        return expiredPoint;
    }

    private User validateUserExists(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }
}

package com.programmers.smrtstore.domain.point.application;


import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.orderManagement.order.application.OrderService;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.point.application.dto.req.AcmPointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.UseCancelPointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
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

        Long orderId = request.getOrderId();
        List<PointResponse> acmHistory = pointFacade.getByOrderIdAndStatus(
            orderId, PointStatus.ACCUMULATED
        );

        // 주문에 대한 상품별 결제금액
        List<OrderedProductResponse> products = orderService.getProductsForOrder(orderId);

        int idx = 0;
        int totalAcmPoint = 0;
        for (OrderedProductResponse product : products) {
            totalAcmPoint += saveAcmPointDetailPerProduct(product, acmHistory, idx, request);
            idx += product.getQuantity();
        }
        return totalAcmPoint;
    }

    private Integer saveAcmPointDetailPerProduct(OrderedProductResponse product,
        List<PointResponse> acmHistory, int idx, AcmPointDetailRequest request) {

        int totalAcmPoint = 0;
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
            idx++;
            totalAcmPoint += pointValue;
            pointDetailRepository.save(pointDetail);
        }
        return totalAcmPoint;
    }

    public Integer saveUseHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        Long userId = request.getUserId();

        PointResponse point = pointFacade.getPointById(pointId);
        Long orderId = point.getOrderId();
        int usedPoint = Math.abs(point.getPointValue());

        // 주문에 대한 상품별 결제금액
        List<OrderedProductResponse> products = orderService.getProductsForOrder(orderId);

        // 상품별 결제금액에 대한 비율
        List<Integer> pointPieces = getPointPiecesPerOrder(products, usedPoint);

        // 포인트 차감을 위한 사용 가능한 포인트 상세 적립 이력
        List<PointDetailCustomResponse> history = pointFacade.getSumGroupByOriginAcmId(userId);

        int idx = 0;
        for (int piece : pointPieces) {
            saveUsedPointDetailPerPiece(piece, history, idx, products, request);
            if (idx < history.size() - 1) {
                idx++;
            }
        }
        return usedPoint;
    }

    private void saveUsedPointDetailPerPiece(int piece, List<PointDetailCustomResponse> history,
        int idx, List<OrderedProductResponse> products, PointDetailRequest request) {

        while (piece != 0) {
            PointDetailCustomResponse acmHistory = history.get(idx);
            int pointAmount = calculateDeductedPoint(acmHistory.getPointAmount(), piece);
            PointDetail pointDetail = request.toEntity(
                products.get(idx).getOrderedProductId(),
                pointFacade.makeNegativeNumber(pointAmount),
                acmHistory.getOriginAcmId()
            );
            pointDetailRepository.save(pointDetail);
            piece -= pointAmount;
        }
    }

    private int calculateDeductedPoint(int pointAmount, int usedPoint) {
        return Math.min(usedPoint, pointAmount);
    }

    private List<Integer> getPointPiecesPerOrder(List<OrderedProductResponse> products, int usePoint) {

        List<Double> productRatio = getOrderedProductRatio(products);
        List<Integer> pointPieces = new ArrayList<>();

        // 상품별 비율에 따른 사용할 포인트의 비율 계산
        for (int idx = 0; idx < productRatio.size() - 1; idx++) {
            int price = (int) (usePoint * productRatio.get(idx));
            pointPieces.add(price);
            usePoint -= price;
        }
        pointPieces.add(usePoint);
        return pointPieces;
    }

    private List<Double> getOrderedProductRatio(List<OrderedProductResponse> products) {

        List<Double> ratios = new ArrayList<>();

        int totalPrice = products.stream()
            .mapToInt(OrderedProductResponse::getTotalPrice)
            .sum();

        double remain = 1.0;
        for (int idx = 0; idx < products.size() - 1; idx++) {
            Integer originPrice = products.get(idx).getTotalPrice();
            double ratio = originPrice.doubleValue() / totalPrice;

            BigDecimal ratioBigDecimal = new BigDecimal(Double.toString(ratio));
            double roundedRatio = ratioBigDecimal.setScale(2, RoundingMode.DOWN).doubleValue();
            ratios.add(roundedRatio);
            remain -= roundedRatio;
        }
        ratios.add(remain);
        return ratios;
    }

    public Long saveUseCancelHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        PointResponse point = pointFacade.getPointById(request.getPointId());
        int canceledPoint = Math.abs(point.getPointValue());

        List<PointDetailResponse> usedDetailHistory = pointFacade.getUsedDetailByOrderId(point.getOrderId());

        Long pointDetailId = null;
        for (PointDetailResponse pointDetail : usedDetailHistory) {
            while (canceledPoint != 0) {
                PointDetail canceledDetail = request.toEntity(
                    Math.abs(pointDetail.getPointValue()),
                    pointDetail.getOriginAcmId());
                pointDetailRepository.save(canceledDetail);
                if (pointDetailId == null) {
                    pointDetailId = pointDetail.getId();
                }

                PointResponse originAcmPoint = pointFacade.getPointById(request.getPointId());
                if (pointFacade.validateExpiredAt(originAcmPoint)) {
                    PointDetail expiredDetail = request.toEntity(
                        pointDetail.getPointValue(),
                        pointDetail.getOriginAcmId());
                    pointDetailRepository.save(expiredDetail);
                }
                canceledPoint += pointDetail.getPointValue();
            }
        }
        return pointDetailId;
    }

    public Long saveExpirationHistory() {

        List<ExpiredPointDetailResponse> expireHistory = pointFacade.getExpiredSumGroupByOriginAcmId();

        Long pointDetailId = null;
        for (ExpiredPointDetailResponse expireDetail : expireHistory) {
            PointDetail pointDetail = PointDetail.makeExpirationHistory(expireDetail);
            pointDetailRepository.save(pointDetail);
            if (pointDetailId == null) {
                pointDetailId = pointDetail.getId();
            }
        }
        return pointDetailId;
    }

    private User validateUserExists(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }
}

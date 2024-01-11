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

    public Long saveUseHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        Long userId = request.getUserId();

        List<PointDetailCustomResponse> history = pointFacade.getSumGroupByOriginAcmId(userId);

        PointResponse point = pointFacade.getPointById(pointId);
        int usedPoint = Math.abs(point.getPointValue());

        Long pointDetailId = null;
        for (PointDetailCustomResponse response : history) {
            while (usedPoint != 0) {
                int pointAmount = calculateDeductedPoint(response.getPointAmount(), usedPoint);
                PointDetail pointDetail = request.toEntity(
                    pointFacade.makeNegativeNumber(pointAmount),
                    response.getOriginAcmId()
                );
                pointDetailRepository.save(pointDetail);
                usedPoint -= pointAmount;
                if (pointDetailId == null) {
                    pointDetailId = pointDetail.getId();
                }
            }
        }
        return pointDetailId;
    }

    private int calculateDeductedPoint(int pointAmount, int usedPoint) {
        return Math.min(usedPoint, pointAmount);
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

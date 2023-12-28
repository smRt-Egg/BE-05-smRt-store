package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponPublicationType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CustomerManageBenefitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.vo.CouponValue;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon_TB")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CouponValue couponValue;

    @Column(nullable = false)
    private boolean membershipCouponYn;

    @Column(nullable = false)
    private boolean duplicationYn;

    @Column(nullable = false)
    private boolean availableYn;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Enumerated(EnumType.STRING)
    private BenefitUnitType benefitUnitType;

    @Enumerated(EnumType.STRING)
    private CustomerManageBenefitType customerManageBenefitType;

    @Enumerated(EnumType.STRING)
    private CouponPublicationType couponPublicationType; //쿠폰 발행방식 -> 유저에게 바로 뿌리기 / 제품에 걸어놓고 다운로드

    @Column(nullable = false)
    private LocalDateTime validPeriodStartDate;

    @Column(nullable = false)
    private LocalDateTime validPeriodEndDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    private CouponQuantity couponQuantity;

    @Builder
    private Coupon(CouponValue couponValue, boolean membershipCouponYn, boolean duplicationYn, boolean availableYn, CouponType couponType, BenefitUnitType benefitUnitType, CustomerManageBenefitType customerManageBenefitType, CouponPublicationType couponPublicationType, LocalDateTime validPeriodStartDate, LocalDateTime validPeriodEndDate, CouponQuantity couponQuantity) {
        this.couponValue = couponValue;
        this.membershipCouponYn = membershipCouponYn;
        this.duplicationYn = duplicationYn;
        this.availableYn = availableYn;
        this.couponType = couponType;
        this.benefitUnitType = benefitUnitType;
        this.customerManageBenefitType = customerManageBenefitType;
        this.couponPublicationType = couponPublicationType;
        this.validPeriodStartDate = validPeriodStartDate;
        this.validPeriodEndDate = validPeriodEndDate;
        this.createdAt = LocalDateTime.now();
        this.couponQuantity = couponQuantity;
    }

    public void validateCoupon() {
        validateEndDate();
        validateAvailable();
    }

    private  void validateAvailable() {
        if (!availableYn) {
            throw new CouponException(ErrorCode.COUPON_NOT_AVAILABLE, String.valueOf(availableYn));
        }
    }

    private  void validateEndDate() {
        if (validPeriodEndDate.isAfter(LocalDateTime.now())) {
            throw new CouponException(ErrorCode.COUPON_DATE_INVALID, validPeriodEndDate.toString());
        }
    }

    public Long discountProduct(Product product) { //-> discount 금액이 product detail 페이지에선 보여줘야함

        /**
         * 쿠폰 계산만 하는게 아니라 어떤 조합이 좋을지 계산을 해야됨. -> 추후 구현 예정
         *if (BenefitUnitType.AMOUNT == benefitUnitType) {
         *             return couponValue.getBenefitValue();
         *         }
         *         return price * (couponValue.getBenefitValue());
         */
        return null;

    }

    public void validMinPrice(Integer price) { //product detail에서 하나의 제품에 쿠폰이 되는지?
        if (price < couponValue.getMinOrderPrice()) {
            throw new CouponException(ErrorCode.ORDER_PRICE_NOT_ENOUGH, String.valueOf(price));
        }
    }

    public void makeAvailableYes() {
        availableYn = true;
    }

    public void makeAvailableNo() {
        availableYn = false;
    }
}

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
    @Column(nullable = false)
    private CouponType couponType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitUnitType benefitUnitType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerManageBenefitType customerManageBenefitType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponPublicationType couponPublicationType;

    @Column(nullable = false)
    private LocalDateTime validPeriodStartDate;

    @Column(nullable = false)
    private LocalDateTime validPeriodEndDate;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "coupon_quantity_id")
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

    //TODO: 오직 Product 단일 페이지에서 사용될 Product 할인 메서드
    // 주문페이지 쿠폰 계산은 아예 따로 -> 여러개 쿠폰과 여러개 product를 복합적으로 계산해야함
    public Long discountProduct(Product product) {
        validateMinPrice(product.getPrice());
        if (couponType == CouponType.SHIPPING_PRICE) {
            return couponValue.getBenefitValue();
        }
        Long discountPrice = 0L;
        switch (benefitUnitType) {
            case AMOUNT:
                discountPrice = discountAmount(product.getPrice());
            case PERCENT:
                discountPrice = discountPercent(product.getPrice());
        }
        return discountPrice;
    }

    public void makeAvailableYes() { //admin 개발하면 그때 검증 로직 추가 예정
        availableYn = true;
    }

    public void makeAvailableNo() {
        availableYn = false;
    }

    public void validateCoupon() {
        validateEndDate();
        validateAvailable();
    }

    private void validateAvailable() {
        if (!availableYn) {
            throw new CouponException(ErrorCode.COUPON_NOT_AVAILABLE);
        }
    }

    private void validateEndDate() {
        if (validPeriodEndDate.isBefore(LocalDateTime.now())) {
            throw new CouponException(ErrorCode.COUPON_DATE_INVALID);
        }
    }

    private Long discountPercent(Integer price) {
        Long discountPrice = couponValue.getBenefitValue() * price/100;
        if (discountPrice < couponValue.getMaxDiscountValue())
            return discountPrice;
        else
            return couponValue.getMaxDiscountValue();

    }

    private Long discountAmount(Integer price) {
        if (couponValue.getBenefitValue() > price) {
            return price.longValue();
        } else
            return couponValue.getBenefitValue();

    }

    private void validateMinPrice(Integer price) {
        if (price < couponValue.getMinOrderPrice()) {
            throw new CouponException(ErrorCode.ORDER_PRICE_NOT_ENOUGH);
        }
    }

}

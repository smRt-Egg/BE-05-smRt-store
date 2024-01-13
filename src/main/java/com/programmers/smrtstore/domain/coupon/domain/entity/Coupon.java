package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponPublicationType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CustomerManageBenefitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.vo.CouponValue;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
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

    private static final Integer DISCOUNT_ZERO = 0;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_quantity_id")
    private CouponQuantity couponQuantity;

    @Builder
    private Coupon(CouponValue couponValue, boolean membershipCouponYn, boolean duplicationYn, boolean availableYn, CouponType couponType, BenefitUnitType benefitUnitType, CustomerManageBenefitType customerManageBenefitType, CouponPublicationType couponPublicationType, LocalDateTime validPeriodStartDate, LocalDateTime validPeriodEndDate, CouponQuantity couponQuantity) {
        validatePercentValue(benefitUnitType, couponValue.getBenefitValue());
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
    public Integer discountProduct(Integer price) {
        if (couponType.equals(CouponType.DELIVERY)) {
            return DISCOUNT_ZERO;
        }
        Integer discountPrice = DISCOUNT_ZERO;
        switch (benefitUnitType) {
            case AMOUNT:
                discountPrice = discountAmount(price);
            case PERCENT:
                discountPrice = discountPercent(price);
        }
        return discountPrice;
    }

    public void makeAvailableYes(User user) {
        if (validPeriodEndDate.isAfter(LocalDateTime.now())) {
            validateAdmin(user);
            availableYn = true;
        }
    }

    public void makeAvailableNo(User user) {
        if (validPeriodEndDate.isBefore(LocalDateTime.now()) && availableYn) {
            validateAdmin(user);
            availableYn = false;
        }
    }

    public void validateCoupon() {
        validateEndDate();
        validateAvailable();
    }

    public boolean validateMinPrice(Integer price) {
        return price < couponValue.getMinOrderPrice() ? false : true;
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

    private Integer discountPercent(Integer price) {
        Integer discountPrice = calculateDiscountValue(price);
        if (discountPrice < couponValue.getMaxDiscountValue())
            return discountPrice;
        else
            return couponValue.getMaxDiscountValue();

    }

    private Integer discountAmount(Integer price) {
        if (couponValue.getBenefitValue() > price) {
            return price;
        } else
            return couponValue.getBenefitValue();

    }

    private void validatePercentValue(BenefitUnitType benefitUnitType, Integer value) {
        if (benefitUnitType == BenefitUnitType.PERCENT && value > 100) {
            throw new CouponException(ErrorCode.COUPON_PERCENT_EXCEED);
        }
    }

    private void validateAdmin(User user) {
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new CouponException(ErrorCode.SECURITY_ACCESS_DENIED);
        }
    }

    private Integer calculateDiscountValue(Integer price) {
        return couponValue.getBenefitValue() * price / 100;
    }

}

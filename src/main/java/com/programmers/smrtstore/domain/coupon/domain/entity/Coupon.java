package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponRequest;
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
    private boolean membershipCouponYn;  //멤버십 쿠폰은 멤버십 유저만 사용 가능하다.

    @Column(nullable = false)
    private boolean duplicationYn; //중복 가능한 쿠폰인지 ? 중복쿠폰 사용하더라도

    @Column(nullable = false)
    private boolean availableYn; // 현재 유효한 쿠폰인지? 쿠폰 사용을 운영자가 아예 막아버릴수도있다!

    @Enumerated(EnumType.STRING)
    private CouponType couponType; //장바구니 쿠폰인지, 배송비 할인 쿠폰인지

    @Enumerated(EnumType.STRING)
    private BenefitUnitType benefitUnitType; //고정금액 할인? 퍼센트 할인?

    @Enumerated(EnumType.STRING)
    private CustomerManageBenefitType customerManageBenefitType; //모든고객?첫구매?재구매?마케팅동의고객?

    @Enumerated(EnumType.STRING)
    private CouponPublicationType couponPublicationType; //쿠폰 발행방식 -> 유저에게 바로 뿌리기 / 제품에 걸어놓고 다운로드

    @Column(nullable = false)
    private LocalDateTime validPeriodStartDate;

    @Column(nullable = false)
    private LocalDateTime validPeriodEndDate; //스프링 이벤트로 end date되면 쿠폰 valid 변경하기?

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "coupon", fetch = FetchType.LAZY)
    private CouponQuantity couponQuantity;

    @Builder
    private Coupon(CouponValue couponValue, boolean membershipCouponYn, boolean duplicationYn, boolean availableYn, CouponType couponType, BenefitUnitType benefitUnitType, CustomerManageBenefitType customerManageBenefitType, CouponPublicationType couponPublicationType, LocalDateTime validPeriodStartDate, LocalDateTime validPeriodEndDate, LocalDateTime createdAt, CouponQuantity couponQuantity) {
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
        this.createdAt = createdAt;
        this.couponQuantity = couponQuantity;
    }

    public static Coupon from(CreateCouponRequest request) {

        return new Coupon(request.getCouponValue(),
                request.isMembershipCouponYn(),
                request.isDuplicationYn(),
                request.isAvailableYn(),
                request.getCouponType(),
                request.getBenefitUnitType(),
                request.getCustomerManageBenefitType(),
                request.getCouponPublicationType(),
                request.getValidPeriodStartDate(),
                request.getValidPeriodEndDate(),
                request.getCreatedAt(),
                request.getCouponQuantity());
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
        Integer price = product.getPrice();
        validMinPrice(price);

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
}

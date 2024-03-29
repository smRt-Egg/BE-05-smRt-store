package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon_available_user_TB")
public class CouponAvailableUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private boolean useYn;

    @Column(nullable = false)
    private Integer issueCount;

    private CouponAvailableUser(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
        this.useYn = false;
        this.issueCount = 1;
    }

    public static CouponAvailableUser of(Coupon coupon, User user) {
        validateCouponWithUser(coupon, user);
        return new CouponAvailableUser(coupon, user);
    }

    public void reIssueCoupon() {
        validateCouponWithUser(coupon, user);
        validateCouponIssueCount();
        validateExistCoupon();

        useYn = false;
        increaseIssueCount();
    }

    public void useCoupon() {
        validateCouponWithUser(coupon, user);
        validateCouponUse();
        useYn = true;
    }

    private void increaseIssueCount() {
        issueCount += 1;
    }

    public static void validateCouponWithUser(Coupon coupon, User user) {
        coupon.validateCoupon();
        // validCustomerManageBenefitType(coupon.getCustomerManageBenefitType()
        validateMembership(coupon.isMembershipCouponYn(), user.getMembershipYn()); //멤버십 쿠폰일때 멤버십 유저인지?
    }

    private static void validateMembership(boolean couponMembership, boolean userMembership) {
        if (couponMembership && !userMembership) {
            throw new CouponException(ErrorCode.COUPON_MEMBERSHIP_USER_ONLY);
        }
    }

//    private void validateCustomerManageBenefitType(CustomerManageBenefitType type) {
//          User.getXXX());//쿠폰에 맞는 고객인지?-> User,ORDER랑 같이 NEW,ALL,TALK 등 USER TYPE에 대한 개발 필요
//    }

    private void validateExistCoupon() {
        if (!useYn) {
            throw new CouponException(ErrorCode.COUPON_EXIST_BY_USER);
        }
    }

    private void validateCouponIssueCount() {
        if (coupon.getCouponValue().getIdPerIssuableCount() <= issueCount) {
            throw new CouponException(ErrorCode.COUPON_ISSUE_COUNT_EXCEED, String.valueOf(issueCount));
        }
    }

    private void validateCouponUse() {
        if (useYn) {
            throw new CouponException(ErrorCode.COUPON_ALREADY_USED);
        }
    }


}

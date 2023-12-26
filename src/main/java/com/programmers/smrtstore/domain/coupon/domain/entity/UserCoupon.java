package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon_owned_user_TB")
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean useYn;

    @Column(nullable = false)
    private Integer issueCount;

    private UserCoupon(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
        this.useYn = false;
        this.issueCount = 1;
    }

    public static UserCoupon of(Coupon coupon, User user) {
        validCouponWithUser(coupon, user);
        return new UserCoupon(coupon, user);
    }

    public void reIssueCoupon() {
        validCouponWithUser(coupon, user);
        validCouponIssueCount();
        validExistCoupon();

        useYn = false;
        increaseIssueCount();
    }

    public void useCoupon() {
        validCouponWithUser(coupon, user);
        validCouponUse();
        useYn = true;
    }

    private void increaseIssueCount() {
        issueCount += 1;
    }

    public static void validCouponWithUser(Coupon coupon, User user) {
        coupon.validCoupon();
        // validCustomerManageBenefitType(coupon.getCustomerManageBenefitType()
        validMembership(coupon.isMembershipCouponYn(), user.isMembershipYN()); //멤버십 쿠폰일때 멤버십 유저인지?
    }

    private static void validMembership(boolean couponMembership, boolean userMembership) {
        if (couponMembership && !userMembership) {
            throw new CouponException(ErrorCode.NOT_MEMBERSHIP, String.valueOf(userMembership));
        }
    }

//    private void validCustomerManageBenefitType(CustomerManageBenefitType type) {
//          User.getXXX());//쿠폰에 맞는 고객인지?-> User,ORDER랑 같이 NEW,ALL,TALK 등 USER TYPE에 대한 개발 필요
//    }

    private void validExistCoupon() {
        if (!useYn) {
            throw new CouponException(ErrorCode.COUPON_EXIST, String.valueOf(useYn));
        }
    }

    private void validCouponIssueCount() {
        if (coupon.getCouponValue().getIdPerIssuableCount() <= issueCount) {
            throw new CouponException(ErrorCode.ISSUE_COUNT_EXCEED, String.valueOf(issueCount));
        }
    }

    private void validCouponUse() {
        if (useYn) {
            throw new CouponException(ErrorCode.COUPON_ALREADY_USE, String.valueOf(useYn));
        }
    }


}

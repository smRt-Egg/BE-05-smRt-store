package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "User_Coupon_TB",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueUserAndCoupon",columnNames = {"user_id","coupon_id"})})
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user ;

    @Column(nullable = false)
    private boolean ownYn;

    @Column(nullable = false)
    private Integer reIssueCount;

    public UserCoupon(Coupon coupon, User user, boolean ownYn, Integer reIssueCount) {
        this.coupon = coupon;
        this.user = user;
        this.ownYn = ownYn;
        this.reIssueCount = reIssueCount;
    }

    public void makeOwnYes() {
        this.ownYn = true;
    }
    public void makeOwnNo() {
        this.ownYn = false;
    }

    public void increaseIssueCount() {
        this.reIssueCount+=1;
    }
}

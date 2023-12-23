package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "coupon_owned_user_TB",
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
    private User user;

    @Column(nullable = false)
    private boolean useYn;

    @Column(nullable = false)
    private Integer issueCount;

    @Builder
    public UserCoupon(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
        this.useYn = false;
        this.issueCount = 1;
    }

    public void downloadCoupon() {
        useYn = false;
        increaseIssueCount();
    }
    public void useCoupon() {
        useYn = true;
    }

    public void increaseIssueCount() {
        issueCount+=1;
    }

}

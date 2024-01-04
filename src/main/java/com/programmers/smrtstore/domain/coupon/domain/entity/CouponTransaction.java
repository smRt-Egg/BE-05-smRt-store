package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponStatus;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class CouponTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected CouponStatus couponStatus;

    @CreationTimestamp
    protected LocalDateTime createAt;
}

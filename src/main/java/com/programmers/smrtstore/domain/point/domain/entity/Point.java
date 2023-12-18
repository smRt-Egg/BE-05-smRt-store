package com.programmers.smrtstore.domain.point.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "point_status", nullable = false)
    private PointStatus pointStatus;

    @Column(name = "point_amount", nullable = false)
    private int pointAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issued_at", nullable = false)
    private Date issuedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at", nullable = false)
    private Date expiredAt;
}

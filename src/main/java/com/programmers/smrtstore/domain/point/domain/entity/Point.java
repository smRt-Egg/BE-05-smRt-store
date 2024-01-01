package com.programmers.smrtstore.domain.point.domain.entity;

import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_TB")
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

    @Column(name = "point_value", nullable = false)
    private int pointValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "membership_apply_yn", nullable = false)
    private Boolean membershipApplyYn;

    @Builder
    private Point(Long userId, Long orderId, PointStatus pointStatus, int pointValue, Boolean membershipApplyYn) {
        this.userId = userId;
        this.orderId = orderId;
        this.pointStatus = pointStatus;
        this.pointValue = pointValue;
        this.issuedAt = LocalDateTime.now();
        this.expiredAt = !pointStatus.equals(PointStatus.ACCUMULATED) ? null : issuedAt.plusYears(10)
            .withHour(0)
            .withMinute(0)
            .withSecond(0);
        this.membershipApplyYn = membershipApplyYn;
    }
}

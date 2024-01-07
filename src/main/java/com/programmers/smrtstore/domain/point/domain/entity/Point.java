package com.programmers.smrtstore.domain.point.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
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
    private Integer pointValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "membership_apply_yn", nullable = false)
    private Boolean membershipApplyYn;

    @Builder
    private Point(Long userId, Long orderId, PointStatus pointStatus, Integer pointValue, Boolean membershipApplyYn) {
        validatePointValue(pointStatus, pointValue);
        this.userId = userId;
        this.orderId = orderId;
        this.pointStatus = pointStatus;
        this.pointValue = pointValue;
        this.issuedAt = LocalDateTime.now();
        this.expiredAt = setExpiredAt(pointStatus);
        this.membershipApplyYn = membershipApplyYn;
    }

    private LocalDateTime setExpiredAt(PointStatus pointStatus) {
        return !pointStatus.equals(PointStatus.ACCUMULATED) ? null : issuedAt.plusYears(10)
            .withHour(0)
            .withMinute(0)
            .withSecond(0);
    }

    private static void validatePointValue(PointStatus pointStatus, Integer pointValue) {

        if (pointStatus.equals(PointStatus.ACCUMULATED) || pointStatus.equals(PointStatus.USE_CANCELED)) {
            if (pointValue < 0) {
                throw new PointException(ErrorCode.POINT_ILLEGAL_ARGUMENT, String.valueOf(pointValue));
            }
        } else {
            if (pointValue > 0) {
                throw new PointException(ErrorCode.POINT_ILLEGAL_ARGUMENT, String.valueOf(pointValue));
            }

            if (Math.abs(pointValue) > PointService.MAX_AVAILALBE_USE_POINT) {
                throw new PointException(ErrorCode.POINT_AVAILALBE_RANGE_EXCEED, String.valueOf(pointValue));
            }
        }
    }
}

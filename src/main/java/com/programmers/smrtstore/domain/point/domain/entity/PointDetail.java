package com.programmers.smrtstore.domain.point.domain.entity;

import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_detail_transaction_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "ordered_product_id")
    private Long orderedProductId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "point_amount", nullable = false)
    private Integer pointAmount;

    @Column(name = "origin_acm_id", nullable = false)
    private Long originAcmId;

    @Builder
    private PointDetail(Long pointId, Long orderedProductId, Long userId, Integer pointAmount, Long originAcmId) {
        this.pointId = pointId;
        this.orderedProductId = orderedProductId;
        this.userId = userId;
        this.pointAmount = pointAmount;
        this.originAcmId = originAcmId;
    }

    public static PointDetail makeExpirationHistory(ExpiredPointDetailResponse expireDetail) {
        return PointDetail.builder()
            .pointId(null)
            .orderedProductId(null)
            .userId(expireDetail.getUserId())
            .pointAmount(expireDetail.getPointAmount() * -1)
            .originAcmId(expireDetail.getOriginAcmId())
            .build();
    }
}

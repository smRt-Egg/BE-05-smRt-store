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
@Table(name = "point_detail_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_detail_id")
    private Long id;

    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "point_amount")
    private Integer pointAmount;

    @Column(name = "origin_acm_id")
    private Long originAcmId;

    @Builder
    private PointDetail(Long pointId, Long userId, Integer pointAmount, Long originAcmId) {
        this.pointId = pointId;
        this.userId = userId;
        this.pointAmount = pointAmount;
        this.originAcmId = originAcmId;
    }

    public static PointDetail makeExpirationHistory(ExpiredPointDetailResponse expireDetail) {
        return PointDetail.builder()
            .pointId(null)
            .userId(expireDetail.getUserId())
            .pointAmount(expireDetail.getPointAmount() * -1)
            .originAcmId(expireDetail.getOriginAcmId())
            .build();
    }
}

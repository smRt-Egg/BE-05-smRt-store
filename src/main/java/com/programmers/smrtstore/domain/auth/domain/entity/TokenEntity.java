package com.programmers.smrtstore.domain.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "refresh_token_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expiry_date", nullable = false)
    private Date refreshTokenExpiryDate;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "auth_id", nullable = false)
    private Auth auth;

    @Builder
    private TokenEntity(String refreshToken, Date refreshTokenExpiryDate, Auth auth) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryDate = refreshTokenExpiryDate;
        this.auth = auth;
    }

    public void updateRefreshToken(String refreshToken, Date refreshTokenExpiryDate) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryDate = refreshTokenExpiryDate;
    }
}

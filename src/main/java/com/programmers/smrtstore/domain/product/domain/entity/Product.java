package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Setter
@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "thumbnail_image", nullable = false)
    private URL thumbnailImage;

    @Column(name = "detail_image")
    private URL detailImage;

    @Column(name = "origin", nullable = false, length = 50)
    private String origin;

    @Column(name = "manufacture_date", nullable = false)
    private LocalDate manufactureDate;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "create_at", nullable = false)
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private LocalDateTime updateAt;

    @Column(name = "is_available", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean isAvailable;
}
package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Setter
@Getter
@Entity
@Table(name = "product_TB")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "thumbnail", nullable = false)
    private URL thumbnail;

    @Column(name = "content_image")
    private URL contentImage;

    @Column(name = "origin", nullable = false, length = 50)
    private String origin;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column(name = "available_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean availableYn;

    @Builder
    Product(String name, Integer price, Category category, URL thumbnail, URL contentImage,
        String origin) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.thumbnail = thumbnail;
        this.contentImage = contentImage;
        this.origin = origin;
        // TODO: 추상화?
        this.createdAt = LocalDateTime.now();
    }
}
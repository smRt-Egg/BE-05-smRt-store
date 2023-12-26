package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "product_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "available_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean availableYn;

    @Column(name = "option_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean optionYn;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_quantity")
    private ProductQuantity productQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>();

    @Builder
    private Product(String name, Integer price, Integer stockQuantity, Category category,
        URL thumbnail, URL contentImage, String origin) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.thumbnail = thumbnail;
        this.contentImage = contentImage;
        this.origin = origin;
        this.productQuantity = ProductQuantity.builder()
            .stockQuantity(stockQuantity == null ? 0 : stockQuantity)
            .build();
    }

    public void addOption(ProductOption productOption) {
        optionYn = true;
        productOptions.add(productOption);

    }

    public void addStockQuantity(Integer quantity) {
        productQuantity.addStockQuantity(quantity);
    }

    public void addStockQuantity(Integer quantity, ProductOption productOption) {
        productOptions.stream().filter(option -> option.equals(productOption))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .addStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity) {
        productQuantity.removeStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity, ProductOption productOption) {
        productOptions.stream().filter(option -> option.equals(productOption))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .removeStockQuantity(quantity);
    }

    public void removeOption(ProductOption productOption) {
        productOptions.remove(productOption);
        if (productOptions.isEmpty()) {
            optionYn = false;
        }
    }

    public Integer getStockQuantity() {
        if (optionYn) {
            return productOptions.stream()
                .map(ProductOption::getStockQuantity)
                .reduce(0, Integer::sum);
        }
        return productQuantity.getStockQuantity();
    }

    public void releaseProduct() {
        if (availableYn || releaseDate != null) {
            throw new RuntimeException();
        }
        this.availableYn = true;
        this.releaseDate = LocalDate.now();
    }

    public void makeNotAvailable() {
        if (!availableYn) {
            throw new RuntimeException();
        }
        this.availableYn = false;
    }

    public void makeAvailable() {
        if (availableYn) {
            throw new RuntimeException();
        }
        this.availableYn = true;
    }
}
package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

    @Column(name = "sale_price", nullable = false)
    private Integer salePrice;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_quantity")
    private ProductQuantity productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "thumbnail", nullable = false)
    private URL thumbnail;

    @Column(name = "content_image")
    private URL contentImage;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductOption> productOptions;

    @Builder
    private Product(String name, Integer salePrice, Integer stockQuantity, Category category,
        URL thumbnail, URL contentImage, boolean optionYn) {
        this.name = name;
        this.salePrice = salePrice;
        this.category = category;
        this.productQuantity = ProductQuantity.from(stockQuantity == null ? 0 : stockQuantity);
        this.thumbnail = thumbnail;
        this.contentImage = contentImage;
        this.optionYn = optionYn;
        if (optionYn) {
            this.productOptions = new ArrayList<>();
        }
    }

    public void addOption(ProductOption productOption) {
        productOptions.add(productOption);

    }

    public void addStockQuantity(Integer quantity) {
        productQuantity.addStockQuantity(quantity);
    }

    public void addStockQuantity(Integer quantity, Long productOptionId) {
        productOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .addStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity) {
        productQuantity.removeStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity, Long productOptionId) {
        productOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .removeStockQuantity(quantity);
    }

    public void removeOption(ProductOption productOption) {
        productOptions.remove(productOption);
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
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_RELEASED);
        }
        this.availableYn = true;
        this.releaseDate = LocalDate.now();
    }

    public void makeNotAvailable() {
        if (!availableYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }
        this.availableYn = false;
    }

    public void makeAvailable() {
        if (availableYn) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_AVAILABLE);
        }
        this.availableYn = true;
    }

    private void updateName(String name) {
        this.name = name;
    }

    private void updateSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    private void updateStockQuantity(Integer stockQuantity) {
        this.productQuantity.updateStockQuantity(stockQuantity);
    }

    private void updateCategory(Category category) {
        this.category = category;
    }

    private void updateThumbnail(URL thumbnail) {
        this.thumbnail = thumbnail;
    }

    private void updateContentImage(URL contentImage) {
        this.contentImage = contentImage;
    }

    public void updateValues(String name, Integer salePrice, Integer stockQuantity,
        Category category, URL thumbnail, URL contentImage) {
        if (name != null) {
            updateName(name);
        }
        if (salePrice != null) {
            updateSalePrice(salePrice);
        }
        if (stockQuantity != null) {
            updateStockQuantity(stockQuantity);
        }
        if (category != null) {
            updateCategory(category);
        }
        if (thumbnail != null) {
            updateThumbnail(thumbnail);
        }
        if (contentImage != null) {
            updateContentImage(contentImage);
        }
    }
}
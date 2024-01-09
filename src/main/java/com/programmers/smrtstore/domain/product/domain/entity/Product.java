package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNameTypes;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "discount_ratio", nullable = false)
    private Integer discountRatio;

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
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status_type", nullable = false)
    private ProductStatusType productStatusType;

    @Column(name = "discount_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean discountYn;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<ProductDetailOption> productDetailOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductAdditionalOption> productAdditionalOptions = new ArrayList<>();

    @Column(name = "combination_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean combinationYn = false;

    @Embedded
    private OptionNameTypes optionNameTypes;

    @Builder
    private Product(String name, Integer price, Category category, boolean combinationYn,
        URL thumbnail, URL contentImage, String optionNameType1, String optionNameType2,
        String optionNameType3) {
        this.name = name;
        this.price = price;
        this.discountRatio = 0;
        this.combinationYn = combinationYn;
        this.category = category;
        this.thumbnail = thumbnail;
        this.contentImage = contentImage;
        this.discountYn = false;
        this.productStatusType = ProductStatusType.NOT_SALE;
        if (combinationYn) {
            this.optionNameTypes = OptionNameTypes.builder()
                .optionNameType1(optionNameType1)
                .optionNameType2(optionNameType2)
                .optionNameType3(optionNameType3)
                .build();
        }
    }

    public Integer getSalePrice() {
        if (discountYn) {
            return price - (price * discountRatio / 100);
        }
        return price;
    }

    public Integer getSalePrice(Long productOptionId) {
        if (discountYn) {
            var productOption = this.productDetailOptions.stream()
                .filter(option -> option.getId().equals(productOptionId))
                .findAny()
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
            return price - ((price + productOption.getPrice()) * discountRatio / 100);
        }
        return price;
    }

    public void addOption(ProductDetailOption detailOption) {
        if (!this.combinationYn && productDetailOptions.size() == 1) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_USE_OPTION);
        }
        productDetailOptions.add(detailOption);
    }

    public void addAdditionalOption(ProductAdditionalOption additionalOption) {
        productAdditionalOptions.add(additionalOption);
    }

    public void increaseStockQuantity(Integer quantity) {
        if (this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_OPTION);
        }
        var option = productDetailOptions.stream().findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.addStockQuantity(quantity);
        productStatusType = ProductStatusType.SALE;
    }

    public void increaseDetailStockQuantity(Integer quantity, Long productOptionId) {
        productDetailOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .addStockQuantity(quantity);
    }

    public void increaseAdditionalStockQuantity(Integer quantity, Long productOptionId) {
        productAdditionalOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .addStockQuantity(quantity);
    }

    public void decreaseStockQuantity(Integer quantity) {
        if (this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_OPTION);
        }
        var option = productDetailOptions.stream().findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.removeStockQuantity(quantity);
        if (option.getStockQuantity() == 0) {
            productStatusType = ProductStatusType.OUT_OF_STOCK;
        }
    }

    public void decreaseDetailStockQuantity(Integer quantity, Long productOptionId) {
        productDetailOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .removeStockQuantity(quantity);
    }

    public void decreaseAdditionalStockQuantity(Integer quantity, Long productOptionId) {
        productAdditionalOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .removeStockQuantity(quantity);
    }

    public void removeOption(Long productOptionId) {
        if (!this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_SINGLE_OPTION);
        }
        var productOption = productDetailOptions.stream()
            .filter(option -> option.getId().equals(productOptionId)).findAny()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        productDetailOptions.remove(productOption);
    }

    public void removeAdditionalOption(Long productOptionId) {
        var additionalOption = productAdditionalOptions.stream()
            .filter(option -> option.getId().equals(productOptionId)).findAny()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        productAdditionalOptions.remove(additionalOption);
    }

    public Integer getStockQuantity() {
        if (combinationYn) {
            return productDetailOptions.stream()
                .map(ProductOption::getStockQuantity)
                .reduce(0, Integer::sum);
        }
        return productDetailOptions.stream().findAny()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .getStockQuantity();
    }

    public void releaseProduct() {
        if (!productStatusType.equals(ProductStatusType.NOT_SALE) || releaseDate != null) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_RELEASED);
        }
        this.productStatusType = ProductStatusType.SALE;
        this.releaseDate = LocalDate.now();
    }

    public void makeNotAvailable() {
        if (productStatusType.equals(ProductStatusType.NOT_SALE)) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }
        this.productStatusType = ProductStatusType.NOT_SALE;
    }

    public void makeAvailable() {
        if (!productStatusType.equals(ProductStatusType.NOT_SALE)) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_AVAILABLE);
        }
        if (releaseDate == null) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_RELEASED);
        }
        this.productStatusType = ProductStatusType.SALE;
    }

    public void updateName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public void updatePrice(Integer price) {
        if (price != null && price > 0) {
            this.price = price;
        }
    }

    public void updateStockQuantity(Integer stockQuantity) {
        if (stockQuantity != null && stockQuantity >= 0) {
            this.productDetailOptions.stream().findAny()
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
                .updateStockQuantity(stockQuantity);
        }
    }

    public void updateCategory(Category category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void updateThumbnail(URL thumbnail) {
        if (thumbnail != null) {
            this.thumbnail = thumbnail;
        }
    }

    public void updateOptionNameTypes(String optionNameType1, String optionNameType2,
        String optionNameType3) {
        if (combinationYn) {
            optionNameTypes.updateOptionNameTypes(optionNameType1, optionNameType2,
                optionNameType3);
        }
    }

    public void updateContentImage(URL contentImage) {
        if (contentImage != null) {
            this.contentImage = contentImage;
        }
    }

    public void updateDiscountRatio(Integer discountRatio) {
        if (discountRatio == 0 && !discountYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_DISCOUNTED);
        }
        if (discountRatio < 0 || discountRatio >= 100) {
            throw new ProductException(ErrorCode.PRODUCT_DISCOUNT_RATIO_NOT_VALID);
        }
        this.discountRatio = discountRatio;
        this.discountYn = discountRatio != 0;
    }

}
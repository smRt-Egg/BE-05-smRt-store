package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionTag;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
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

    private static final Integer SINGLE_OPTION_PRICE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "discount_ratio", nullable = false)
    private Float discountRatio;

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

    @Column(name = "available_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean availableYn;

    @Column(name = "discount_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean discountYn;

    @Column(name = "combination_yn", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean combinationYn;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<ProductOption> productOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductOption> productAdditionalOptions = new ArrayList<>();


    @Builder
    private Product(String name, Integer price, Category category, boolean combinationYn,
        Integer stockQuantity, URL thumbnail, URL contentImage) {
        this.name = name;
        this.price = price;
        this.discountRatio = 0f;
        this.combinationYn = combinationYn;
        this.category = category;
        this.thumbnail = thumbnail;
        this.contentImage = contentImage;
        this.discountYn = false;
        if (!combinationYn) {
            ProductOption.builder()
                .optionType(OptionType.SUPPLEMENTED)
                .optionName(name)
                .optionTag(OptionTag.SINGLE)
                .stockQuantity(stockQuantity)
                .price(SINGLE_OPTION_PRICE)
                .product(this)
                .build();
        }
    }

    public Integer getSalePrice() {
        if (discountYn) {
            return price - (int) (price * discountRatio / 100);
        }
        return price;
    }

    public Integer getSalePrice(Long productOptionId) {
        if (discountYn) {
            var productOption = this.productOptions.stream()
                .filter(option -> option.getId().equals(productOptionId))
                .findAny()
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
            return price - (int) ((price + productOption.getPrice()) * discountRatio / 100);
        }
        return price;
    }

    public void addOption(ProductOption productOption) {
        if (!this.combinationYn && productOptions.size() == 1) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_USE_OPTION);
        }
        productOptions.add(productOption);
    }

    public void addAdditionalOption(ProductOption productOption) {
        if (productOption.getOptionType().equals(OptionType.SUPPLEMENTED)) {
            throw new ProductException(ErrorCode.PRODUCT_OPTION_TYPE_INVALID);
        }
        productAdditionalOptions.add(productOption);
    }

    public void addStockQuantity(Integer quantity) {
        if (this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_OPTION);
        }
        var option = productOptions.stream().findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.addStockQuantity(quantity);
    }

    public void addStockQuantity(Integer quantity, Long productOptionId) {
        productOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .addStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity) {
        if (this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_OPTION);
        }
        var option = productOptions.stream().findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.removeStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity, Long productOptionId) {
        productOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .removeStockQuantity(quantity);
    }

    public void removeOption(ProductOption productOption) {
        if (!this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_SINGLE_OPTION);
        }
        productOptions.remove(productOption);
    }

    public Integer getStockQuantity() {
        if (combinationYn) {
            return productOptions.stream()
                .map(ProductOption::getStockQuantity)
                .reduce(0, Integer::sum);
        }
        return productOptions.stream().findAny()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .getStockQuantity();
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
        if (releaseDate == null) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_RELEASED);
        }
        this.availableYn = true;
    }

    private void updateName(String name) {
        this.name = name;
    }

    private void updatePrice(Integer price) {
        this.price = price;
    }

    private void updateStockQuantity(Integer stockQuantity) {
        this.productOptions.stream().findAny()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .updateStockQuantity(stockQuantity);
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

    public void updateValues(String name, Integer price, Integer stockQuantity,
        Category category, URL thumbnail, URL contentImage) {
        if (name != null) {
            updateName(name);
        }
        if (price != null) {
            updatePrice(price);
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

    public void updateDiscountRatio(Float discountRatio) {
        if (discountRatio == 0) {
            throw new ProductException(ErrorCode.PRODUCT_DISCOUNT_RATIO_NOT_VALID);
        }
        this.discountRatio = discountRatio;
        this.discountYn = true;
    }

    public void disableDiscount() {
        if (discountRatio == 0 && !discountYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_DISCOUNTED);
        }
        this.discountRatio = 0f;
        this.discountYn = false;
    }
}
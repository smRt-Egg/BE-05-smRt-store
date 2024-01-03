package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
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

    private static final Integer SINGLE_OPTION_PRICE = 0;
    private static final String SINGLE_OPTION_TYPE = "Default";

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
        Integer stockQuantity, URL thumbnail, URL contentImage, String optionNameType1,
        String optionNameType2, String optionNameType3) {
        this.name = name;
        this.price = price;
        this.discountRatio = 0f;
        this.combinationYn = combinationYn;
        this.category = category;
        this.thumbnail = thumbnail;
        this.contentImage = contentImage;
        this.discountYn = false;
        this.productStatusType = ProductStatusType.NOT_SALE;
        if (!combinationYn) {
            this.optionNameTypes = OptionNameTypes.builder()
                .optionType(OptionType.SINGLE)
                .build();
        } else {
            this.optionNameTypes = OptionNameTypes.builder()
                .optionType(OptionType.COMBINATION)
                .optionNameType1(optionNameType1)
                .optionNameType2(optionNameType2)
                .optionNameType3(optionNameType3)
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
            var productOption = this.productDetailOptions.stream()
                .filter(option -> option.getId().equals(productOptionId))
                .findAny()
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
            return price - (int) ((price + productOption.getPrice()) * discountRatio / 100);
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

    public void addStockQuantity(Integer quantity) {
        if (this.combinationYn) {
            throw new ProductException(ErrorCode.PRODUCT_USE_OPTION);
        }
        var option = productDetailOptions.stream().findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.addStockQuantity(quantity);
        productStatusType = ProductStatusType.SALE;
    }

    public void addStockQuantity(Integer quantity, Long productOptionId) {
        productDetailOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .addStockQuantity(quantity);
    }

    public void addAdditionalStockQuantity(Integer quantity, Long productOptionId) {
        productAdditionalOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .addStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity) {
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

    public void removeStockQuantity(Integer quantity, Long productOptionId) {
        productDetailOptions.stream().filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .removeStockQuantity(quantity);
    }

    public void removeAdditionalStockQuantity(Integer quantity, Long productOptionId) {
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

    private void updateName(String name) {
        this.name = name;
    }

    private void updatePrice(Integer price) {
        this.price = price;
    }

    private void updateStockQuantity(Integer stockQuantity) {
        this.productDetailOptions.stream().findAny()
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND))
            .setStockQuantity(stockQuantity);
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
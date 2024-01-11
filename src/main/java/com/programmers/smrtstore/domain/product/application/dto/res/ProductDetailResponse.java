package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNameTypes;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer salePrice;
    private Integer discountRatio;
    private Category category;
    private Integer stockQuantity;
    private URL thumbnail;
    private URL contentImage;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProductStatusType productStatusType;
    private boolean combinationYn;
    private boolean discountYn;
    private OptionNameTypes optionNameTypes;

    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getSalePrice(),
            product.getDiscountRatio(),
            product.getCategory(),
            product.getStockQuantity(),
            product.getThumbnail(),
            product.getContentImage(),
            product.getReleaseDate(),
            product.getCreatedAt(),
            product.getUpdatedAt(),
            product.getProductStatusType(),
            product.isCombinationYn(),
            product.isDiscountYn(),
            product.getOptionNameTypes()
        );
    }
}

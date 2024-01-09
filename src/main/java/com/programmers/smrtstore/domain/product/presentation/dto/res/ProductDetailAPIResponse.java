package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailAPIResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer salePrice;
    private Integer discountRatio;
    private Integer categoryId;
    private Integer stockQuantity;
    private String thumbnailUrl;
    private String contentImageUrl;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String productStatusType;
    private boolean combinationYn;
    private boolean discountYn;
    private String optionNameType1;
    private String optionNameType2;
    private String optionNameType3;
    private Integer optionSize;

    public static ProductDetailAPIResponse from(ProductDetailResponse response){
        return new ProductDetailAPIResponse(
            response.getId(),
            response.getName(),
            response.getPrice(),
            response.getSalePrice(),
            response.getDiscountRatio(),
            response.getCategory().getId(),
            response.getStockQuantity(),
            response.getThumbnail().toString(),
            response.getContentImage().toString(),
            response.getReleaseDate(),
            response.getCreatedAt(),
            response.getUpdatedAt(),
            response.getProductStatusType().name(),
            response.isCombinationYn(),
            response.isDiscountYn(),
            response.getOptionNameTypes().getOptionNameType1(),
            response.getOptionNameTypes().getOptionNameType2(),
            response.getOptionNameTypes().getOptionNameType3(),
            response.getOptionNameTypes().getSize()
        );
    }
}

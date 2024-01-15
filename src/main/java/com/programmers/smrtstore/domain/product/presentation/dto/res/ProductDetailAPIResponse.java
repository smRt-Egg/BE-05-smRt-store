package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailResponse;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNameTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Builder(access = AccessLevel.PRIVATE)
    private ProductDetailAPIResponse(Long id, String name, Integer price, Integer salePrice,
        Integer discountRatio, Integer categoryId, Integer stockQuantity, String thumbnailUrl,
        String contentImageUrl, LocalDate releaseDate, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ProductStatusType productStatusType, boolean combinationYn, boolean discountYn, OptionNameTypes optionNameTypes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
        this.discountRatio = discountRatio;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
        this.thumbnailUrl = thumbnailUrl;
        this.contentImageUrl = contentImageUrl;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productStatusType = productStatusType.name();
        this.combinationYn = combinationYn;
        this.discountYn = discountYn;
        if(optionNameTypes != null){
            this.optionNameType1 = optionNameTypes.getOptionNameType1();
            this.optionNameType2 = optionNameTypes.getOptionNameType2();
            this.optionNameType3 = optionNameTypes.getOptionNameType3();
            this.optionSize = optionNameTypes.getSize();
        }

    }

    public static ProductDetailAPIResponse from(ProductDetailResponse response){
        return ProductDetailAPIResponse.builder()
            .id(response.getId())
            .name(response.getName())
            .price(response.getPrice())
            .salePrice(response.getSalePrice())
            .discountRatio(response.getDiscountRatio())
            .categoryId(response.getCategory().getId())
            .stockQuantity(response.getStockQuantity())
            .thumbnailUrl(response.getThumbnail())
            .contentImageUrl(response.getContentImage())
            .releaseDate(response.getReleaseDate())
            .createdAt(response.getCreatedAt())
            .updatedAt(response.getUpdatedAt())
            .productStatusType(response.getProductStatusType())
            .combinationYn(response.isCombinationYn())
            .discountYn(response.isDiscountYn())
            .optionNameTypes(response.getOptionNameTypes())
            .build();
    }
}
package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNameTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductAPIResponse {

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

    private List<ProductDetailOptionAPIResponse> detailOptionAPIResponses;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductAPIResponse(Long id, String name, Integer price, Integer salePrice,
        Integer discountRatio, Category category, Integer stockQuantity, String thumbnailUrl,
        String contentImageUrl, LocalDate releaseDate, LocalDateTime createdAt,
        LocalDateTime updatedAt, ProductStatusType productStatusType, boolean combinationYn,
        boolean discountYn, OptionNameTypes optionNameTypes,
        List<ProductDetailOptionResponse> detailOptionResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
        this.discountRatio = discountRatio;
        this.categoryId = category.getId();
        this.stockQuantity = stockQuantity;
        this.thumbnailUrl = thumbnailUrl;
        this.contentImageUrl = contentImageUrl;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productStatusType = productStatusType.name();
        this.combinationYn = combinationYn;
        this.discountYn = discountYn;
        if (optionNameTypes != null) {
            this.optionNameType1 = optionNameTypes.getOptionNameType1();
            this.optionNameType2 = optionNameTypes.getOptionNameType2();
            this.optionNameType3 = optionNameTypes.getOptionNameType3();
            this.optionSize = optionNameTypes.getSize();
        }

        this.detailOptionAPIResponses = detailOptionResponses.stream()
            .map(ProductDetailOptionAPIResponse::from).toList();
    }

    public static ProductAPIResponse from(ProductResponse productResponse) {
        return ProductAPIResponse.builder()
            .id(productResponse.getId())
            .name(productResponse.getName())
            .price(productResponse.getPrice())
            .salePrice(productResponse.getSalePrice())
            .discountRatio(productResponse.getDiscountRatio())
            .category(productResponse.getCategory())
            .stockQuantity(productResponse.getStockQuantity())
            .thumbnailUrl(productResponse.getThumbnail())
            .contentImageUrl(productResponse.getContentImage())
            .releaseDate(productResponse.getReleaseDate())
            .createdAt(productResponse.getCreatedAt())
            .updatedAt(productResponse.getUpdatedAt())
            .productStatusType(productResponse.getProductStatusType())
            .combinationYn(productResponse.isCombinationYn())
            .discountYn(productResponse.isDiscountYn())
            .optionNameTypes(productResponse.getOptionNameTypes())
            .detailOptionResponses(productResponse.getDetailOptionResponses())
            .build();
    }

}
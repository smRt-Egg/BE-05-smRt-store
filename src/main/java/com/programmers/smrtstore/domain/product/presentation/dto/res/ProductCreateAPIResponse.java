package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCreateAPIResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer salePrice;
    private Float discountRatio;
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
    private List<ProductAdditionalOptionAPIResponse> additionalOptionAPIResponses;

    public static ProductCreateAPIResponse from(ProductResponse productResponse) {
        return new ProductCreateAPIResponse(
            productResponse.getId(),
            productResponse.getName(),
            productResponse.getPrice(),
            productResponse.getSalePrice(),
            productResponse.getDiscountRatio(),
            productResponse.getCategory().getId(),
            productResponse.getStockQuantity(),
            productResponse.getThumbnail().toString(),
            productResponse.getContentImage().toString(),
            productResponse.getReleaseDate(),
            productResponse.getCreatedAt(),
            productResponse.getUpdatedAt(),
            productResponse.getProductStatusType().name(),
            productResponse.isCombinationYn(),
            productResponse.isDiscountYn(),
            productResponse.getOptionNameTypes().getOptionNameType1(),
            productResponse.getOptionNameTypes().getOptionNameType2(),
            productResponse.getOptionNameTypes().getOptionNameType3(),
            productResponse.getOptionNameTypes().getSize(),
            productResponse.getDetailOptionResponses().stream()
                .map(ProductDetailOptionAPIResponse::from).toList(),
            productResponse.getAdditionalOptionResponses().stream()
                .map(ProductAdditionalOptionAPIResponse::from).toList()
        );
    }

}

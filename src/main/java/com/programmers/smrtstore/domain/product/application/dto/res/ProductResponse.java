package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResponse {

    private Long id;
    private String name;
    private Integer salePrice;
    private Category category;
    private Integer stockQuantity;
    private URL thumbnail;
    private URL contentImage;
    private LocalDate releaseDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean availableYn;
    private boolean optionYn;
    private List<ProductOptionResponse> productOptions;


    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getSalePrice(),
            product.getCategory(),
            product.getStockQuantity(),
            product.getThumbnail(),
            product.getContentImage(),
            product.getReleaseDate(),
            product.getCreatedAt(),
            product.getUpdatedAt(),
            product.isAvailableYn(),
            product.isOptionYn(),
            product.getProductOptions() == null ? null : product.getProductOptions().stream().map(ProductOptionResponse::from).toList()
        );
    }
}

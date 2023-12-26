package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateProductResponse {

    private Long id;
    private String name;
    private Integer salePrice;
    private Category category;
    private Integer stockQuantity;
    private URL thumbnail;
    private URL contentImage;
    private boolean availableYn;
    private boolean optionYn;
    private LocalDate releaseDate;
    private Timestamp createdAt;
    private Timestamp updatedAT;


    public static UpdateProductResponse from(Product product) {
        return new UpdateProductResponse(product.getId(),
            product.getName(),
            product.getSalePrice(),
            product.getCategory(),
            product.getStockQuantity(),
            product.getThumbnail(),
            product.getContentImage(),
            product.isAvailableYn(),
            product.isOptionYn(),
            product.getReleaseDate(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}

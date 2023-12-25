package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private Category category;
    private URL thumbnail;
    private URL contentImage;
    private String origin;
    private LocalDate releaseDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean availableYn;


    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .category(product.getCategory())
            .thumbnail(product.getThumbnail())
            .contentImage(product.getContentImage())
            .origin(product.getOrigin())
            .releaseDate(product.getReleaseDate())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .availableYn(product.isAvailableYn())
            .build();
    }
}

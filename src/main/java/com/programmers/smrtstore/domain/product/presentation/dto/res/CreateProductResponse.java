package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import java.net.URL;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private Category category;
    private URL thumbnail;
    private URL contentImage;
    private String origin;
    private LocalDateTime createdAt;
}

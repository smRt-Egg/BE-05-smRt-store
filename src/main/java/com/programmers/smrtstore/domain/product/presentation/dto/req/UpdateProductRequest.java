package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import java.net.URL;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductRequest {
    private String name;
    private Integer price;
    private Category category;
    private URL thumbnail;
    private URL contentImage;
    private String origin;
}

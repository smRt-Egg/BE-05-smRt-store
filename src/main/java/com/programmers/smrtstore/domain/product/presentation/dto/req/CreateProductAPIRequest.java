package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.util.List;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
public class CreateProductAPIRequest {

    @NotBlank(message = "상품 이름은 null 값일 수 없습니다.")
    private String name;

    @NotNull(message = "상품 가격은 null 값일 수 없습니다.")
    @Min(value = 1, message = "상품 가격은 1원 이상이어야 합니다.")
    private Integer price;

    @Min(value = 0, message = "상품 수량은 0개 이상이어야 합니다.")
    private Integer stockQuantity;

    @NotNull(message = "상품 카테고리는 null 값일 수 없습니다.")
    private Integer categoryId;

    @NotNull(message = "상품 섬네일 이미지는 null 값일 수 없습니다.")
    @URL(message = "상품 섬네일 이미지는 url 형식으로 전송되어야 합니다.")
    private String thumbnailUrl;

    @URL(message = "상품 상세 이미지는 url 형식으로 전송되어야 합니다.")
    private String contentImageUrl;

    @NotNull(message = "상품 조합은 null 값일 수 없습니다.")
    private boolean combinationYn;

    private String optionNameType1;
    private String optionNameType2;
    private String optionNameType3;

    @Valid
    private List<CreateProductDetailOptionAPIRequest> productDetailOptions;

    public CreateProductRequest toProductRequest() {
        try {
            return CreateProductRequest.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .category(Category.findById(categoryId))
                .thumbnail(new java.net.URL(thumbnailUrl))
                .contentImage(new java.net.URL(contentImageUrl))
                .combinationYn(combinationYn)
                .optionNameType1(optionNameType1)
                .optionNameType2(optionNameType2)
                .optionNameType3(optionNameType3)
                .build();
        } catch (MalformedURLException ignore) {
        }
        return null;
    }

    public List<CreateProductDetailOptionRequest> toDetailOptionRequests() {
        return productDetailOptions.stream().map(
            CreateProductDetailOptionAPIRequest::toRequest).toList();
    }
}

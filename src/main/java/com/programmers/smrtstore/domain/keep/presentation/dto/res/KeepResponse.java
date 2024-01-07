package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.net.URL;

@Getter
@Builder
public class KeepResponse {
    private Long id;
    private Long userId;
    private String name;
    private Integer salePrice;
    private URL contentImage;

    public KeepResponse(Long id, Long userId, String name, Integer salePrice, URL contentImage) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.salePrice = salePrice;
        this.contentImage = contentImage;
    }
}

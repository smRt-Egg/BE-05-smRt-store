package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyHomeResponse {

    private String nickName;
    private String thumbnail;
    private Integer orderDeliveryCount;
    private Integer cartCount;
    private Integer reviewPoint;
    private Integer couponCount;

    //전체 찜 목록
    private List<Product> keepProducts;
    //카테고리별 찜목록

    //최근 본 상품 목록
}

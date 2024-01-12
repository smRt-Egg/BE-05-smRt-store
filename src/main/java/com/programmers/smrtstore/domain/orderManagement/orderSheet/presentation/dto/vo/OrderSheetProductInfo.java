package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderSheetProductInfo {
    private Long orderedProductId;
    private String productName;
    private Integer orgPrice;
    private Integer immediateDiscount;
    private OrderSheetProductOptionResponse productOption;
    private Integer orderTotalPrice;

    @Builder
    public OrderSheetProductInfo(
        Long orderedProductId, String productName, Integer orgPrice, Integer immediateDiscount,
        OrderSheetProductOptionResponse productOption, Integer orderTotalPrice
    ) {
        this.orderedProductId = orderedProductId;
        this.productName = productName;
        this.orgPrice = orgPrice;
        this.immediateDiscount = immediateDiscount;
        this.productOption = productOption;
        this.orderTotalPrice = orderTotalPrice;
    }

    public static OrderSheetProductInfo from(
        OrderedProduct orderedProduct
    ) {
        return OrderSheetProductInfo.builder()
            .orderedProductId(orderedProduct.getId())
            .productName(orderedProduct.getProduct().getName())
            .orgPrice(orderedProduct.getProduct().getPrice())
            .immediateDiscount(orderedProduct.getImmediateDiscount())
            // TODO: productOption에 있는거 가져다쓰면 문제되나 생각하기
            .productOption(OrderSheetProductOptionResponse.of(
                    orderedProduct.getProductOption().getId(),
                    orderedProduct.getProductOption().getOptionNames(),
                    orderedProduct.getExtraPrice(),
                    orderedProduct.getQuantity(),
                    orderedProduct.getProductOption().getOptionType()
                )
            )
            .orderTotalPrice(orderedProduct.getTotalPrice())
            .build();
    }

}

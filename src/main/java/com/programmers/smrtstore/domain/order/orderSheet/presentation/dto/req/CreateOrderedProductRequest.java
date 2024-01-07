package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.req;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOrderedProductRequest {

    private Long productId;
    private Integer salePrice;
    private Integer sellerImmediateDiscount;
    private List<OrderSheetProductOptionReq> options;
    private boolean optionYn;
}

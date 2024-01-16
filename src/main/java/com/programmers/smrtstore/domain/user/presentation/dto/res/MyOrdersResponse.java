package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderPreviewResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyOrdersResponse {

    private String nickName;

    private String username;

    private Integer point;

    private Integer unwrittenReviewPoint;

    private Integer reviewCount;

    private List<OrderPreviewResponse> orderDeliveryList;
}

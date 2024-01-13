package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import lombok.Getter;

@Getter
public class OrderSheetUserPointInfo {
    private Integer pointBalance;

    public OrderSheetUserPointInfo(Integer pointBalance) {
        this.pointBalance = pointBalance;
    }
}

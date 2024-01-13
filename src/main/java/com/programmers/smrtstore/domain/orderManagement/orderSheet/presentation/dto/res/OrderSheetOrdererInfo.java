package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.res;

import lombok.Getter;

@Getter
public class OrderSheetOrdererInfo {

    private String ordererName;
    private String ordererPhoneNumber;
    private String ordererEmail;

    private OrderSheetOrdererInfo(
        String ordererName, String ordererPhoneNumber, String ordererEmail
    ) {
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
        this.ordererEmail = ordererEmail;
    }

    public static OrderSheetOrdererInfo of(
        String ordererName, String ordererPhoneNumber, String ordererEmail
    ) {
        return new OrderSheetOrdererInfo(ordererName, ordererPhoneNumber, ordererEmail);
    }
}

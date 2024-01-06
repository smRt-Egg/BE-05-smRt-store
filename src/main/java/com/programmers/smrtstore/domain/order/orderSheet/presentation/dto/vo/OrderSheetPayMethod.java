package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.order.domain.entity.enums.PaymentMethod;
import java.util.List;

public class OrderSheetPayMethod {
    private List<PaymentMethod> availablePayMethodTypes;
    private PaymentMethod defaultPayment;
}

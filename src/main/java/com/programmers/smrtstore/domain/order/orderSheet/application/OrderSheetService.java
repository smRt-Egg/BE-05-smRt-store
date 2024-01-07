package com.programmers.smrtstore.domain.order.orderSheet.application;

import com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.req.CreateOrderSheetRequest;
import com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.res.OrderSheetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderSheetService {

    public OrderSheetResponse createOrderSheet(CreateOrderSheetRequest request) {

        return null;
    }

}

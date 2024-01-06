package com.programmers.smrtstore.domain.orderManagement.orderSheet.application;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.CreateOrderSheetRequest;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.res.OrderSheetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderSheetService {

    public Long createOrderSheet(CreateOrderSheetRequest request) {

        return null;
    }

    public OrderSheetResponse getOrderSheetById(Long orderSheetId) {

        return null;
    }

    public void deleteOrderSheetById(Long orderSheetId) {

    }

}

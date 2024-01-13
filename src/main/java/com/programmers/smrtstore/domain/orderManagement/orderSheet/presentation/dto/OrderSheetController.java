package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.application.OrderSheetService;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.CreateOrderSheetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order-sheet")
public class OrderSheetController {

    private final OrderSheetService orderSheetService;

    @PostMapping
    public Long createOrderSheet(@RequestBody CreateOrderSheetRequest request) {
        return orderSheetService.createOrderSheet(null, request);
    }
}

package com.programmers.smrtstore.domain.user.presentation.facade;

import com.programmers.smrtstore.domain.user.application.service.ShippingAddressService;
import com.programmers.smrtstore.domain.user.application.service.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DurationRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final ShippingAddressService shippingAddressService;

    @Override
    public ProfileUserResponse getUserInfo(Long userId) {
        return userService.getUserInfo(userId);
    }

    @Override
    public ProfileUserResponse update(Long userId, UpdateUserRequest request) {
        return userService.update(userId, request);
    }

    @Override
    public void withdraw(Long userId) {
        userService.withdraw(userId);
    }

    @Override
    public MyHomeResponse getMyHome(Long userId) {
        return userService.getMyHome(userId);
    }

    @Override
    public DetailShippingResponse createShippingAddress(Long userId,
        DetailShippingRequest request) {
        return shippingAddressService.createShippingAddress(userId, request);
    }

    @Override
    public DeliveryAddressBook getShippingAddressList(Long userId) {
        return shippingAddressService.getShippingAddressList(userId);
    }

    @Override
    public DetailShippingResponse updateShippingAddress(Long userId, Long shippingId,
        UpdateShippingRequest request) {
        return shippingAddressService.updateShippingAddress(userId, shippingId, request);
    }

    @Override
    public DetailShippingResponse findByShippingId(Long shippingId) {
        return shippingAddressService.findByShippingId(shippingId);
    }

    @Override
    public void deleteShippingAddress(Long userId, Long shippingId) {
        shippingAddressService.deleteShippingAddress(userId, shippingId);
    }

    @Override
    public String sendCodeToEmail(String userEmail) {
        return userService.sendCodeToEmail(userEmail);
    }

    @Override
    public void verifyCode(String userEmail, String code) {
        userService.verifyCode(userEmail, code);
    }

    @Override
    public MyOrdersResponse getOrders(Long userId) {
        return userService.getOrders(userId);
    }

    @Override
    public MyAllKeepsResponse getMyAllKeeps(Long userId) {
        return userService.getMyAllKeeps(userId);
    }

    @Override
    public MyCategoryKeepsResponse getMyKeepsByCategory(Long userId, Integer categoryId) {
        return userService.getMyKeepsByCategory(userId, categoryId);
    }

    @Override
    public MyReviewsResponse getMyReviews(Long userId, DurationRequest request) {
        return userService.getMyReviews(userId, request);
    }

    @Override
    public MyWritableReviewsResponse getMyWritableReviews(Long userId) {
        return userService.getMyWritableReviews(userId);
    }

    @Override
    public MyQnaResponse getMyQna(Long userId, DurationRequest request) {
        return userService.getMyQna(userId, request);
    }

    @Override
    public MyOrdersResponse getPurchasedConfirmedOrders(Long userId) {
        return userService.getPurchasedConfirmedOrders(userId);
    }

    @Override
    public MyCouponsResponse getMyAllCoupons(Long userId) {
        return userService.getMyAllCoupons(userId);
    }
}

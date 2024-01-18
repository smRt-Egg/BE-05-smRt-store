package com.programmers.smrtstore.domain.user.presentation.facade;

import com.programmers.smrtstore.domain.user.application.service.ShippingAddressService;
import com.programmers.smrtstore.domain.user.application.service.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
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
}

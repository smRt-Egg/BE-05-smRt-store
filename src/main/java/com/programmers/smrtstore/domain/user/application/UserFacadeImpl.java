package com.programmers.smrtstore.domain.user.application;

import com.programmers.smrtstore.domain.user.domain.entity.User;
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
        User user = userService.findByUserId(userId);
        return shippingAddressService.createShippingAddress(user, request);
    }

    @Override
    public DeliveryAddressBook getShippingAddressList(Long userId) {
        User user = userService.findByUserId(userId);
        return shippingAddressService.getShippingAddressList(user);
    }

    @Override
    public DetailShippingResponse updateShippingAddress(Long userId, Long shippingId,
        UpdateShippingRequest request) {
        User user = userService.findByUserId(userId);
        return shippingAddressService.updateShippingAddress(user, shippingId, request);
    }

    @Override
    public DetailShippingResponse findByShippingId(Long shippingId) {
        return shippingAddressService.findByShippingId(shippingId);
    }

    @Override
    public void deleteShippingAddress(Long userId, Long shippingId) {
        User user = userService.findByUserId(userId);
        shippingAddressService.deleteShippingAddress(user, shippingId);
    }
}

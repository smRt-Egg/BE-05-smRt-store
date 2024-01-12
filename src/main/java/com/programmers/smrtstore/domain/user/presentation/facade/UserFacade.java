package com.programmers.smrtstore.domain.user.presentation.facade;

import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;

public interface UserFacade {

    ProfileUserResponse getUserInfo(Long userId);

    ProfileUserResponse update(Long userId, UpdateUserRequest request);

    void withdraw(Long userId);

    DetailShippingResponse createShippingAddress(Long userId, DetailShippingRequest request);

    DeliveryAddressBook getShippingAddressList(Long userId);

    DetailShippingResponse updateShippingAddress(Long userId, Long shippingId,
        UpdateShippingRequest request);

    DetailShippingResponse findByShippingId(Long shippingId);

    void deleteShippingAddress(Long userId, Long shippingId);

    String sendCodeToEmail(String userEmail);

    void verifyCode(String userEmail, String code);
}

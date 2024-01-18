package com.programmers.smrtstore.domain.user.presentation.facade;

import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DurationRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.*;

public interface UserFacade {

    ProfileUserResponse getUserInfo(Long userId);

    ProfileUserResponse update(Long userId, UpdateUserRequest request);

    void withdraw(Long userId);

    MyHomeResponse getMyHome(Long userId);

    DetailShippingResponse createShippingAddress(Long userId, DetailShippingRequest request);

    DeliveryAddressBook getShippingAddressList(Long userId);

    DetailShippingResponse updateShippingAddress(Long userId, Long shippingId,
        UpdateShippingRequest request);

    DetailShippingResponse findByShippingId(Long shippingId);

    void deleteShippingAddress(Long userId, Long shippingId);

    String sendCodeToEmail(String userEmail);

    void verifyCode(String userEmail, String code);

    MyOrdersResponse getOrders(Long userId);

    MyAllKeepsResponse getMyAllKeeps(Long userId);

    MyCategoryKeepsResponse getMyKeepsByCategory(Long userId, Integer categoryId);

    MyReviewsResponse getMyReviews(Long userId, DurationRequest request);

    MyWritableReviewsResponse getMyWritableReviews(Long userId);

    MyQnaResponse getMyQna(Long userId, DurationRequest request);

    MyOrdersResponse getPurchasedConfirmedOrders(Long userId);

    MyCouponsResponse getMyAllCoupons(Long userId);
}

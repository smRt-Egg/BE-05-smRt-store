package com.programmers.smrtstore.domain.user.presentation.controller;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.user.application.service.MyHomeService;
import com.programmers.smrtstore.domain.user.application.service.ShippingAddressService;
import com.programmers.smrtstore.domain.user.application.service.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DurationRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyAllKeepsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyCategoryKeepsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyCouponsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyHomeResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyOrdersResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyQnaResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyReviewsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyWritableReviewsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MyHomeService myHomeService;
    private final ShippingAddressService shippingAddressService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileUserResponse> profile(
        @UserId Long userId) {
        ProfileUserResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ProfileUserResponse> update(
        @UserId Long userId, @RequestBody @Valid
    UpdateUserRequest request) {
        ProfileUserResponse response = userService.update(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(@UserId Long userId) {
        userService.withdraw(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verification-request")
    public ResponseEntity<Void> sendMail(@RequestParam("email") @Valid @Email String email) {
        userService.sendCodeToEmail(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verification")
    public ResponseEntity<String> verify(@RequestParam("email") @Valid @Email String userEmail,
        @RequestParam("code") String code) {
        userService.verifyCode(userEmail, code);
        return ResponseEntity.ok("인증에 성공했습니다.");
    }

    @GetMapping("/home")
    public ResponseEntity<MyHomeResponse> myHome(@UserId Long userId) {
        MyHomeResponse response = myHomeService.getMyHome(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/orders")
    public ResponseEntity<MyOrdersResponse> myOrders(@UserId Long userId) {
        MyOrdersResponse response = myHomeService.getOrders(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/orders/purchased-confirmed")
    public ResponseEntity<MyOrdersResponse> myPurchasedConfirmedOrders(@UserId Long userId) {
        MyOrdersResponse response = myHomeService.getPurchasedConfirmedOrders(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/reviews")
    public ResponseEntity<MyReviewsResponse> myReviews(@UserId Long userId, @RequestBody @Valid DurationRequest request) {
        MyReviewsResponse response = myHomeService.getMyReviews(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/writable-reviews")
    public ResponseEntity<MyWritableReviewsResponse> myWritableReviews(@UserId Long userId) {
        MyWritableReviewsResponse response = myHomeService.getMyWritableReviews(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/qna")
    public ResponseEntity<MyQnaResponse> myQna(@UserId Long userId, @RequestBody @Valid
    DurationRequest request) {
        MyQnaResponse response = myHomeService.getMyQna(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/keeps")
    public ResponseEntity<MyAllKeepsResponse> myAllKeeps(@UserId Long userId) {
        MyAllKeepsResponse response = myHomeService.getMyAllKeeps(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/keeps/{categoryId}")
    public ResponseEntity<MyCategoryKeepsResponse> myKeepsByCategory(@UserId Long userId, @PathVariable Integer categoryId) {
        MyCategoryKeepsResponse response = myHomeService.getMyKeepsByCategory(userId, categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my/coupons")
    public ResponseEntity<MyCouponsResponse> myAllCoupons(@UserId Long userId) {
        MyCouponsResponse response = myHomeService.getMyAllCoupons(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/shipping")
    public ResponseEntity<DetailShippingResponse> createShippingAddress(
        @UserId Long userId,
        @RequestBody @Valid DetailShippingRequest request) {
        DetailShippingResponse response = shippingAddressService.createShippingAddress(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping")
    public ResponseEntity<DeliveryAddressBook> shippingAddressList(
        @UserId Long userId) {
        DeliveryAddressBook response = shippingAddressService.getShippingAddressList(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/shipping/{shippingId}")
    public ResponseEntity<DetailShippingResponse> updateDefaultShippingAddress(@UserId Long userId,
        @PathVariable Long shippingId, @RequestBody @Valid UpdateShippingRequest request) {
        DetailShippingResponse response = shippingAddressService.updateShippingAddress(userId, shippingId,
            request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping/{shippingId}")
    public ResponseEntity<DetailShippingResponse> findByShippingId(@PathVariable Long shippingId) {
        DetailShippingResponse response = shippingAddressService.findByShippingId(shippingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/shipping/{shippingId}")
    public ResponseEntity<Void> deleteShippingAddress(@UserId Long userId,
        @PathVariable Long shippingId) {
        shippingAddressService.deleteShippingAddress(userId, shippingId);
        return ResponseEntity.noContent().build();
    }
}

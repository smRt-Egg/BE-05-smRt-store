package com.programmers.smrtstore.domain.user.presentation;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileUserResponse> profile(
        @UserId Long userId) {
        ProfileUserResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<ProfileUserResponse> update(
        @UserId Long userId, @RequestBody
    UpdateUserRequest request) {
        ProfileUserResponse response = userService.update(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(@UserId Long userId) {
        userService.withdraw(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shipping")
    public ResponseEntity<DetailShippingResponse> createShippingAddress(
        @UserId Long userId,
        @RequestBody @Valid DetailShippingRequest request) {
        DetailShippingResponse response = userService.createShippingAddress(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping")
    public ResponseEntity<DeliveryAddressBook> shippingAddressList(
        @UserId Long userId) {
        DeliveryAddressBook response = userService.getShippingAddressList(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping/{shippingId}")
    public ResponseEntity<DetailShippingResponse> findByShippingId(@PathVariable Long shippingId) {
        DetailShippingResponse response = userService.findByShippingId(shippingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/shipping/{shippingId}")
    public ResponseEntity<Void> deleteShippingAddress(@PathVariable Long shippingId) {
        userService.deleteShippingAddress(shippingId);
        return ResponseEntity.noContent().build();
    }
}

package com.programmers.smrtstore.domain.user.presentation;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.CreateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.CreateShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
    public ResponseEntity<CreateShippingResponse> createShippingAddress(
        @UserId Long userId,
        @RequestBody @Valid CreateShippingRequest request) {
        CreateShippingResponse response = userService.createShippingAddress(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping")
    public ResponseEntity<DeliveryAddressBook> shippingAddressList(
        @UserId Long userId) {
        DeliveryAddressBook response = userService.getShippingAddressList(userId);
        return ResponseEntity.ok(response);
    }
}

package com.programmers.smrtstore.domain.user.presentation.controller;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
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

    private final UserFacade userFacade;

    @GetMapping("/profile")
    public ResponseEntity<ProfileUserResponse> profile(
        @UserId Long userId) {
        ProfileUserResponse response = userFacade.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<ProfileUserResponse> update(
        @UserId Long userId, @RequestBody @Valid
    UpdateUserRequest request) {
        ProfileUserResponse response = userFacade.update(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(@UserId Long userId) {
        userFacade.withdraw(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shipping")
    public ResponseEntity<DetailShippingResponse> createShippingAddress(
        @UserId Long userId,
        @RequestBody @Valid DetailShippingRequest request) {
        DetailShippingResponse response = userFacade.createShippingAddress(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping")
    public ResponseEntity<DeliveryAddressBook> shippingAddressList(
        @UserId Long userId) {
        DeliveryAddressBook response = userFacade.getShippingAddressList(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("shipping/{shippingId}")
    public ResponseEntity<DetailShippingResponse> updateDefaultShippingAddress(@UserId Long userId,
        @PathVariable Long shippingId, @RequestBody @Valid UpdateShippingRequest request) {
        DetailShippingResponse response = userFacade.updateShippingAddress(userId, shippingId,
            request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipping/{shippingId}")
    public ResponseEntity<DetailShippingResponse> findByShippingId(@PathVariable Long shippingId) {
        DetailShippingResponse response = userFacade.findByShippingId(shippingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/shipping/{shippingId}")
    public ResponseEntity<Void> deleteShippingAddress(@UserId Long userId,
        @PathVariable Long shippingId) {
        userFacade.deleteShippingAddress(userId, shippingId);
        return ResponseEntity.noContent().build();
    }
}

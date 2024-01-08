package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.DEFAULT_SHIPPING_NOT_DELETABLE;
import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_SHIPPING_ADDRESS;
import static com.programmers.smrtstore.core.properties.ErrorCode.EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS;
import static com.programmers.smrtstore.core.properties.ErrorCode.SHIPPING_ADDRESS_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.user.application.vo.AggUserShippingInfo;
import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.ShippingAddressJpaRepository;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        return ProfileUserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }

    public ProfileUserResponse update(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        user.updateUser(request);
        return ProfileUserResponse.from(user);
    }

    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        user.saveDeleteDate();
    }
}

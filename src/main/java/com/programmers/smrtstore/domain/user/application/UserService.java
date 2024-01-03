package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_SHIPPING_ADDRESS;
import static com.programmers.smrtstore.core.properties.ErrorCode.EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse.from;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.CreateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.CreateShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private static final int MAXIMUM_SHIPPING_SIZE = 15;


    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        return from(user);
    }

    public ProfileUserResponse update(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        user.updateUser(request);
        return from(user);
    }

    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        user.saveDeleteDate();
    }

    public CreateShippingResponse createShippingAddress(Long userId, CreateShippingRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();

        checkShippingAddressesSize(shippingAddresses);
        ShippingAddress shippingAddress = request.toShippingAddressEntity(user);
        checkShippingDuplicate(shippingAddress, shippingAddresses);
        user.addShippingAddress(shippingAddress);

        return CreateShippingResponse.from(shippingAddress);
    }

    private void checkShippingAddressesSize(List<ShippingAddress> shippingAddresses) {
        if(shippingAddresses.size() >= MAXIMUM_SHIPPING_SIZE)
            throw new UserException(EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS, String.valueOf(MAXIMUM_SHIPPING_SIZE));
    }

    private void checkShippingDuplicate(ShippingAddress shippingAddress, List<ShippingAddress> shippingAddresses) {
        shippingAddresses.forEach(address -> {
            if(address.getName().equals(shippingAddress.getName())
                && address.getRecipient().equals(shippingAddress.getRecipient())
                && address.getAddress1Depth().equals(shippingAddress.getAddress1Depth())
                && address.getAddress2Depth().equals(shippingAddress.getAddress2Depth())
                && address.getZipCode().equals(shippingAddress.getZipCode())
                && address.getPhoneNum1().equals(shippingAddress.getPhoneNum1())
                && address.getPhoneNum2().equals(shippingAddress.getPhoneNum2()))
                throw new UserException(DUPLICATE_SHIPPING_ADDRESS, String.valueOf(shippingAddress.getId()));
        });
    }
}

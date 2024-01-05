package com.programmers.smrtstore.domain.user.application;

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
import com.programmers.smrtstore.domain.user.presentation.dto.req.CreateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.CreateShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ShippingAddressJpaRepository shippingAddressRepository;
    private static final int MAXIMUM_SHIPPING_SIZE = 15;


    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        return ProfileUserResponse.from(user);
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

    public CreateShippingResponse createShippingAddress(Long userId,
        CreateShippingRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();

        checkShippingAddressesSize(shippingAddresses);
        ShippingAddress shippingAddress = request.toShippingAddressEntity(user);
        checkShippingDuplicate(shippingAddress, shippingAddresses);
        user.addShippingAddress(shippingAddress);
        shippingAddressRepository.save(shippingAddress);

        return CreateShippingResponse.from(shippingAddress);
    }

    private void checkShippingAddressesSize(List<ShippingAddress> shippingAddresses) {
        if (shippingAddresses.size() >= MAXIMUM_SHIPPING_SIZE) {
            throw new UserException(EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS,
                String.valueOf(MAXIMUM_SHIPPING_SIZE));
        }
    }

    private void checkShippingDuplicate(ShippingAddress shippingAddress,
        List<ShippingAddress> shippingAddresses) {
        shippingAddresses.forEach(address -> {
            if ((address.getPhoneNum2() == null && shippingAddress.getPhoneNum2() == null)
                || (address.getPhoneNum2() != null && shippingAddress.getPhoneNum2() != null
                && address.getPhoneNum2().equals(shippingAddress.getPhoneNum2()))) {
                if (address.getName().equals(shippingAddress.getName())
                    && address.getRecipient().equals(shippingAddress.getRecipient())
                    && address.getAddress1Depth().equals(shippingAddress.getAddress1Depth())
                    && address.getAddress2Depth().equals(shippingAddress.getAddress2Depth())
                    && address.getZipCode().equals(shippingAddress.getZipCode())
                    && address.getPhoneNum1().equals(shippingAddress.getPhoneNum1())) {
                    throw new UserException(DUPLICATE_SHIPPING_ADDRESS,
                        String.valueOf(shippingAddress.getId()));
                }
            }
        });
    }

    @Transactional(readOnly = true)
    public DeliveryAddressBook getShippingAddressList(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();
        AggUserShippingInfo separated = separateDefaultShippingAddress(shippingAddresses);
        return DeliveryAddressBook.from(separated);
    }

    private AggUserShippingInfo separateDefaultShippingAddress(
        List<ShippingAddress> shippingAddresses) {
        List<ShippingAddress> notDefaultShippingAddresses = new ArrayList<>();
        ShippingAddress defaultShippingAddress = null;

        for (ShippingAddress address : shippingAddresses) {
            if (!address.isDefaultYn()) {
                notDefaultShippingAddresses.add(address);
            } else {
                defaultShippingAddress = address;
            }
        }

        return AggUserShippingInfo.of(defaultShippingAddress, notDefaultShippingAddresses);
    }

    public CreateShippingResponse findByShippingId(Long shippingId) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingId)
            .orElseThrow(
                () -> new UserException(SHIPPING_ADDRESS_NOT_FOUND, String.valueOf(shippingId)));
        return CreateShippingResponse.from(shippingAddress);
    }
}

package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.SHIPPING_ADDRESS_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.user.application.vo.AggUserShippingInfo;
import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.ShippingAddressJpaRepository;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingAddressService {

    private final ShippingAddressJpaRepository shippingAddressRepository;
    private final UserRepository userRepository;
    public static final int MAXIMUM_SHIPPING_SIZE = 15;

    public DetailShippingResponse createShippingAddress(Long userId,
        DetailShippingRequest request) {
        User user = findByUserId(userId);
        user.checkShippingAddressesSize();
        ShippingAddress shippingAddress = request.toShippingAddressEntity(user);
        user.checkShippingDuplicate(request.toShippingAddressEntity(user));
        if (shippingAddress.getDefaultYn()) {
            user.disableOriginalDefault();
        }
        user.addShippingAddress(shippingAddress);
        shippingAddressRepository.save(shippingAddress);

        return DetailShippingResponse.from(shippingAddress);
    }

    @Transactional(readOnly = true)
    public DeliveryAddressBook getShippingAddressList(Long userId) {
        User user = findByUserId(userId);
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();
        AggUserShippingInfo separated = separateDefaultShippingAddress(shippingAddresses);
        return DeliveryAddressBook.from(separated);
    }

    public DetailShippingResponse updateShippingAddress(Long userId, Long shippingId,
        UpdateShippingRequest request) {
        User user = findByUserId(userId);
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingId)
            .orElseThrow(
                () -> new UserException(SHIPPING_ADDRESS_NOT_FOUND, String.valueOf(shippingId)));

        user.checkShippingDuplicate(request.toShippingAddressEntity(user));
        if (!shippingAddress.getDefaultYn() && request.getDefaultYn()) //기본 배송지 갱신할 경우 기존 기본 배송지 해제
        {
            user.disableOriginalDefault();
        }
        shippingAddress.updateShippingAddress(request);
        return DetailShippingResponse.from(shippingAddress);
    }

    @Transactional(readOnly = true)
    public DetailShippingResponse findByShippingId(Long shippingId) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingId)
            .orElseThrow(
                () -> new UserException(SHIPPING_ADDRESS_NOT_FOUND, String.valueOf(shippingId)));
        return DetailShippingResponse.from(shippingAddress);
    }

    public void deleteShippingAddress(Long userId, Long shippingId) {
        User user = findByUserId(userId);
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingId)
            .orElseThrow(
                () -> new UserException(SHIPPING_ADDRESS_NOT_FOUND, String.valueOf(shippingId)));
        shippingAddress.checkIsDefault();

        user.deleteShippingAddress(shippingId);
        shippingAddressRepository.delete(shippingAddress);
    }

    private AggUserShippingInfo separateDefaultShippingAddress(
        List<ShippingAddress> shippingAddresses) {
        List<ShippingAddress> notDefaultShippingAddresses = new ArrayList<>();
        ShippingAddress defaultShippingAddress = null;

        for (ShippingAddress address : shippingAddresses) {
            if (!address.getDefaultYn()) {
                notDefaultShippingAddresses.add(address);
            } else {
                defaultShippingAddress = address;
            }
        }

        return AggUserShippingInfo.of(defaultShippingAddress, notDefaultShippingAddresses);
    }

    private User findByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }
}

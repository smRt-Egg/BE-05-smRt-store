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
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingAddressService {

    private final ShippingAddressJpaRepository shippingAddressRepository;
    private static final int MAXIMUM_SHIPPING_SIZE = 15;

    public DetailShippingResponse createShippingAddress(User user,
        DetailShippingRequest request) {
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();

        checkShippingAddressesSize(shippingAddresses);
        ShippingAddress shippingAddress = request.toShippingAddressEntity(user);
        checkShippingDuplicate(request, shippingAddresses);
        if (shippingAddress.getDefaultYn()) {
            user.disableOriginalDefault();
        }
        user.addShippingAddress(shippingAddress);
        shippingAddressRepository.save(shippingAddress);

        return DetailShippingResponse.from(shippingAddress);
    }

    @Transactional(readOnly = true)
    public DeliveryAddressBook getShippingAddressList(User user) {
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();
        AggUserShippingInfo separated = separateDefaultShippingAddress(shippingAddresses);
        return DeliveryAddressBook.from(separated);
    }

    public DetailShippingResponse updateShippingAddress(User user, Long shippingId,
        DetailShippingRequest request) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingId)
            .orElseThrow(
                () -> new UserException(SHIPPING_ADDRESS_NOT_FOUND, String.valueOf(shippingId)));

        checkShippingDuplicate(request, user.getShippingAddresses());
        if (!shippingAddress.getDefaultYn() && request.getDefaultYn()) //기본 배송지 갱신할 경우 기존 기본 배송지 해제
            user.disableOriginalDefault();
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

    public void deleteShippingAddress(User user, Long shippingId) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingId)
            .orElseThrow(
                () -> new UserException(SHIPPING_ADDRESS_NOT_FOUND, String.valueOf(shippingId)));
        checkIsDefault(shippingId, shippingAddress);

        user.deleteShippingAddress(shippingId);
        shippingAddressRepository.delete(shippingAddress);
    }

    private void checkIsDefault(Long shippingId, ShippingAddress shippingAddress) {
        if (shippingAddress.getDefaultYn()) {
            throw new UserException(DEFAULT_SHIPPING_NOT_DELETABLE, String.valueOf(shippingId));
        }
    }

    private void checkShippingAddressesSize(List<ShippingAddress> shippingAddresses) {
        if (shippingAddresses.size() >= MAXIMUM_SHIPPING_SIZE) {
            throw new UserException(EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS,
                String.valueOf(MAXIMUM_SHIPPING_SIZE));
        }
    }

    private void checkShippingDuplicate(DetailShippingRequest request,
        List<ShippingAddress> shippingAddresses) {
        shippingAddresses.forEach(address -> {

            if (Objects.equals(request.getPhoneNum2(), address.getPhoneNum2())) {
                if (address.getName().equals(request.getName())
                    && address.getRecipient().equals(request.getRecipient())
                    && address.getAddress1Depth().equals(request.getAddress1Depth())
                    && address.getAddress2Depth().equals(request.getAddress2Depth())
                    && address.getZipCode().equals(request.getZipCode())
                    && address.getPhoneNum1().equals(request.getPhoneNum1())) {
                    throw new UserException(DUPLICATE_SHIPPING_ADDRESS,
                        String.valueOf(address.getId()));
                }
            }
        });
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
}

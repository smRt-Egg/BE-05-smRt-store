package com.programmers.smrtstore.domain.user.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.DEFAULT_SHIPPING_NOT_DELETABLE;
import static com.programmers.smrtstore.core.properties.ErrorCode.INAPPROPRIATE_ADDRESS_1_DEPTH_LENGTH;
import static com.programmers.smrtstore.core.properties.ErrorCode.INAPPROPRIATE_ADDRESS_2_DEPTH_LENGTH;
import static com.programmers.smrtstore.core.properties.ErrorCode.INAPPROPRIATE_PHONE_NUM_FORM;
import static com.programmers.smrtstore.core.properties.ErrorCode.INAPPROPRIATE_RECIPIENT_LENGTH;
import static com.programmers.smrtstore.core.properties.ErrorCode.INAPPROPRIATE_SHIPPING_ADDRESS_NAME_LENGTH;

import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipping_address_TB")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    private String recipient;

    @Column(nullable = false, length = 50)
    private String address1Depth;

    @Column(nullable = false, length = 30)
    private String address2Depth;

    @Column(nullable = false, length = 10)
    private String zipCode;

    @Column(nullable = false, length = 15)
    private String phoneNum1;

    @Column(length = 15)
    private String phoneNum2;

    @Column(nullable = false)
    private Boolean defaultYn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void disableDefault() {
        defaultYn = false;
    }

    public void updateShippingAddress(DetailShippingRequest request) {
        if (request.getName() != null) {
            updateName(request.getName());
        }
        if (request.getRecipient() != null) {
            updateRecipient(request.getRecipient());
        }
        if (request.getAddress1Depth() != null) {
            updateAddress1Depth(request.getAddress1Depth());
        }
        if (request.getAddress2Depth() != null) {
            updateAddress2Depth(request.getAddress2Depth());
        }
        if (request.getZipCode() != null) {
            updateAddress2Depth(request.getZipCode());
        }
        if (request.getPhoneNum1() != null) {
            updatePhoneNum1(request.getPhoneNum1());
        }
        if (request.getPhoneNum2() != null) {
            updatePhoneNum2(request.getPhoneNum2());
        }
        if (request.getDefaultYn() != null) {
            updateDefaultYn(request.getDefaultYn());
        }
    }

    public void checkIsDefault() {
        if (defaultYn) {
            throw new UserException(DEFAULT_SHIPPING_NOT_DELETABLE, String.valueOf(id));
        }
    }

    private void updateDefaultYn(Boolean defaultYn) {
        this.defaultYn = defaultYn;
    }

    private void updatePhoneNum2(String phoneNum2) {
        Pattern phonePattern = Pattern.compile("^01(?:0|1|[6-9])[0-9]{7,8}$");
        Matcher matcher = phonePattern.matcher(phoneNum2);
        if (!matcher.matches()) {
            throw new UserException(INAPPROPRIATE_PHONE_NUM_FORM, phoneNum2);
        }

        this.phoneNum2 = phoneNum2;
    }

    private void updatePhoneNum1(String phoneNum1) {
        Pattern phonePattern = Pattern.compile("^01(?:0|1|[6-9])[0-9]{7,8}$");
        Matcher matcher = phonePattern.matcher(phoneNum1);
        if (!matcher.matches()) {
            throw new UserException(INAPPROPRIATE_PHONE_NUM_FORM, phoneNum1);
        }

        this.phoneNum1 = phoneNum1;
    }

    private void updateAddress2Depth(String address2Depth) {
        if (address1Depth.isEmpty() || recipient.length() > 30) {
            throw new UserException(INAPPROPRIATE_ADDRESS_2_DEPTH_LENGTH, address2Depth);
        }

        this.address2Depth = address2Depth;
    }

    private void updateAddress1Depth(String address1Depth) {
        if (address1Depth.isEmpty() || recipient.length() > 50) {
            throw new UserException(INAPPROPRIATE_ADDRESS_1_DEPTH_LENGTH, address1Depth);
        }

        this.address1Depth = address1Depth;
    }

    private void updateRecipient(String recipient) {
        if (recipient.isEmpty() || recipient.length() > 10) {
            throw new UserException(INAPPROPRIATE_RECIPIENT_LENGTH, recipient);
        }

        this.recipient = recipient;
    }

    private void updateName(String name) {
        if (name.length() > 10) {
            throw new UserException(INAPPROPRIATE_SHIPPING_ADDRESS_NAME_LENGTH, name);
        }

        this.name = name;
    }
}

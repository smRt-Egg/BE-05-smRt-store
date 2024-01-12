package com.programmers.smrtstore.domain.user.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_SHIPPING_ADDRESS;
import static com.programmers.smrtstore.core.properties.ErrorCode.EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS;
import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_AGE;
import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_BIRTH_FORM;
import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_EMAIL_FORM;
import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_NICKNAME_LENGTH;
import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_PHONE_NUM_FORM;
import static com.programmers.smrtstore.core.properties.ErrorCode.UPDATED_POINT_VALUE_INVALID;

import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "user_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;

    @Column(nullable = false, length = 10)
    private String nickName;

    @Column(nullable = false, length = 64)
    @Email
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, length = 64)
    private String birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "BLOB")
    private String thumbnail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private Boolean marketingAgree;

    @Column(nullable = false)
    private Boolean membershipYn;

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private static final int MAXIMUM_SHIPPING_SIZE = 15;
    private static final Pattern emailPattern = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$");
    private static final Pattern birthPattern = Pattern.compile("^\\d{8}$");
    private static final int DEFAULT_ACCUMULATE = 100000;

    public List<GrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority(role.name()))
            .map(GrantedAuthority.class::cast)
            .toList();
    }

    public void updateUser(UpdateUserRequest request) {
        if (request.getAge() != null) {
            updateAge(request.getAge());
        }
        if (request.getNickName() != null) {
            updateNickName(request.getNickName());
        }
        if (request.getEmail() != null) {
            updateEmail(email);
        }
        if (request.getPhone() != null) {
            updatePhone(request.getPhone());
        }
        if (request.getBirth() != null) {
            updateBirth(request.getBirth());
        }
        if (request.getGender() != null) {
            updateGender(request.getGender());
        }
        if (request.getThumbnail() != null) {
            updateThumbnail(request.getThumbnail());
        }
        if (request.getMarketingAgree() != null) {
            updateMarketingAgree(request.getMarketingAgree());
        }
    }

    public void joinMembership() {
        this.membershipYn = true;
    }

    public void withdrawMembership() {
        this.membershipYn = false;
    }

    public void saveDeleteDate() {
        this.deletedAt = LocalDateTime.now();
    }

    public void addShippingAddress(ShippingAddress shippingAddress) {
        shippingAddresses.add(shippingAddress);
    }

    public void disableOriginalDefault() {
        for (ShippingAddress shippingAddress : shippingAddresses) {
            if (shippingAddress.getDefaultYn()) {
                shippingAddress.disableDefault();
                break;
            }
        }
    }

    public void deleteShippingAddress(Long shippingId) {
        shippingAddresses.removeIf(address -> address.getId().equals(shippingId));
    }

    public void checkShippingAddressesSize() {
        if (shippingAddresses.size() >= MAXIMUM_SHIPPING_SIZE) {
            throw new UserException(EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS,
                String.valueOf(MAXIMUM_SHIPPING_SIZE));
        }
    }

    public void checkShippingDuplicate(ShippingAddress requestAddress) {
        shippingAddresses.forEach(address -> {

            if (Objects.equals(requestAddress.getPhoneNum2(), address.getPhoneNum2())) {
                if (address.getName().equals(requestAddress.getName())
                    && address.getRecipient().equals(requestAddress.getRecipient())
                    && address.getAddress1Depth().equals(requestAddress.getAddress1Depth())
                    && address.getAddress2Depth().equals(requestAddress.getAddress2Depth())
                    && address.getZipCode().equals(requestAddress.getZipCode())
                    && address.getPhoneNum1().equals(requestAddress.getPhoneNum1())) {
                    throw new UserException(DUPLICATE_SHIPPING_ADDRESS,
                        String.valueOf(address.getId()));
                }
            }
        });
    }

    public int updatePoint(int pointValue) {
        validatePointValue(pointValue);
        return point += pointValue;
    }

    private void validatePointValue(int pointValue) {
        if(pointValue == 0 || (!membershipYn && (pointValue > DEFAULT_ACCUMULATE))) {
            throw new UserException(UPDATED_POINT_VALUE_INVALID, String.valueOf(pointValue));
        }
    }

    private void updateAge(int age) {
        if (age < 7 || age > 200) {
            throw new UserException(INVALID_AGE, String.valueOf(age));
        }
        this.age = age;
    }

    private void updateNickName(String nickName) {
        if (nickName.isEmpty() || nickName.length() > 10) {
            throw new UserException(INVALID_NICKNAME_LENGTH, nickName);
        }
        this.nickName = nickName;
    }

    private void updateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        if (!matcher.matches()) {
            throw new UserException(INVALID_EMAIL_FORM, email);
        }

        this.email = email;
    }

    private void updateBirth(String birth) {
        Matcher matcher = birthPattern.matcher(birth);
        if (!matcher.matches()) {
            throw new UserException(INVALID_BIRTH_FORM, birth);
        }

        this.birth = birth;
    }

    private void updatePhone(String phone) {
        Pattern phonePattern = Pattern.compile("^01(?:0|1|[6-9])[0-9]{7,8}$");
        Matcher matcher = phonePattern.matcher(phone);
        if (!matcher.matches()) {
            throw new UserException(INVALID_PHONE_NUM_FORM, phone);
        }

        this.phone = phone;
    }

    private void updateGender(Gender gender) {
        this.gender = gender;
    }

    private void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private void updateMarketingAgree(Boolean marketingAgree) {
        this.marketingAgree = marketingAgree;
    }
}

package com.programmers.smrtstore.domain.user.domain.entity;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Min(value = 7, message = "나이는 7살 이상이어야 합니다.")
    @Max(value = 200, message = "나이는 200살 이하여야 합니다.")
    private Integer age;

    @Column(nullable = false, length = 10)
    @Size(min = 1, max = 10, message = "별명은 1~10자여야 합니다.")
    private String nickName;

    @Column(nullable = false, length = 64)
    @Email
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phone;

    @Column(nullable = false, length = 64)
    @Pattern(regexp = "^\\d{8}$", message = "올바른 생년월일 형식이 아닙니다.")
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
    private boolean marketingAgree;

    @Column(nullable = false)
    private boolean membershipYn;

    @Column(nullable = false)
    private boolean repurchaseYn;

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public List<GrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority(role.name()))
            .map(GrantedAuthority.class::cast)
            .toList();
    }

    public void updateUser(UpdateUserRequest request) {
        if(request.getAge() != null)
            this.age = request.getAge();
        if(request.getNickName() != null)
            this.nickName = request.getNickName();
        if(request.getEmail() != null)
            this.email = request.getEmail();
        if(request.getPhone() != null)
            this.phone = request.getPhone();
        if(request.getBirth() != null)
            this.birth = request.getBirth();
        if(request.getGender() != null)
            this.gender = request.getGender();
        if(request.getThumbnail() != null)
            this.thumbnail = request.getThumbnail();
        this.marketingAgree = request.isMarketingAgree();
    }

    public void repurchase() {
        this.repurchaseYn = true;
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
            if (shippingAddress.isDefaultYn()) {
                shippingAddress.disableDefault();
                break;
            }
        }
    }

    public void deleteShippingAddress(Long shippingId) {
        shippingAddresses.removeIf(address -> address.getId().equals(shippingId));
    }
}

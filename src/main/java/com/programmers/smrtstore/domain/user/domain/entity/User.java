package com.programmers.smrtstore.domain.user.domain.entity;

import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;

    @Column(nullable = false, length = 10)
    private String nickName;

    @Column(nullable = false, length = 64)
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
    private boolean marketingAgree;

    @Column(nullable = false)
    private boolean membershipYN;

    @Column(nullable = false)
    private boolean repurchaseYN;

    @OneToMany(mappedBy = "user")
    List<ShippingAddress> shippingAddresses;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public User(Integer age, String birth, String email, Gender gender, Role role,
        String phone, boolean marketingAgree, String nickName, String thumbnail) {
        this.age = age;
        this.birth = birth;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.phone = phone;
        this.marketingAgree = marketingAgree;
        this.nickName = nickName;
        this.thumbnail = thumbnail;
        this.point = 0;
    }

    public List<GrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority(role.name()))
            .map(GrantedAuthority.class::cast)
            .toList();
    }

    public void updateUser(UpdateUserRequest request) {
        this.age = request.getAge();
        this.nickName = request.getNickName();
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.birth = request.getBirth();
        this.gender = request.getGender();
        this.thumbnail = request.getThumbnail();
        this.marketingAgree = request.isMarketingAgree();
    }

    public void repurchase() {
        this.repurchaseYN = true;
    }

    public void joinMembership() {
        this.membershipYN = true;
    }

    public void withdrawMembership() {
        this.membershipYN = false;
    }

    public void saveDeleteDate() {
        this.deletedAt = LocalDateTime.now();
    }

    public void addShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress.isDefaultYN()) {
            unlockOriginalDefault();
        }
        shippingAddresses.add(shippingAddress);
    }

    private void unlockOriginalDefault() {
        for (ShippingAddress shippingAddress : shippingAddresses) {
            if (shippingAddress.isDefaultYN()) {
                shippingAddress.unlockDefault();
                break;
            }
        }
    }
}

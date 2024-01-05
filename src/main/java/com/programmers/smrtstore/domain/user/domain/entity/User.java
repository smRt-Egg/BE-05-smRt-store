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
        if(shippingAddress.isDefaultYn()) {
            disableOriginalDefault();
        }
        shippingAddresses.add(shippingAddress);
    }

    private void disableOriginalDefault() {
        for (ShippingAddress shippingAddress : shippingAddresses) {
            if (shippingAddress.isDefaultYn()) {
                shippingAddress.disableDefault();
                break;
            }
        }
    }
}

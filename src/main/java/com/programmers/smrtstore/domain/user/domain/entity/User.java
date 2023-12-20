package com.programmers.smrtstore.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String loginId;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 10)
    private String nickName;

    @Column(nullable = false, length = 64)
    private String email;

    @Column(nullable = false, length = 64)
    private String birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean marketingAgree;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime deletedAt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isMembership;

    private String image;

    @Column(nullable = false)
    private String phoneNum;

    private Long shippingInfoId;

    @Column(nullable = false)
    private Integer point;

    public List<GrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority(role.name()))
            .map(GrantedAuthority.class::cast)
            .toList();
    }
}

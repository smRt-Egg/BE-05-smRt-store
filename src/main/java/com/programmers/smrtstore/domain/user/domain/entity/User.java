package com.programmers.smrtstore.domain.user.domain.entity;

import static com.programmers.smrtstore.domain.user.domain.entity.Auth.toAuth;

import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id")
    private Auth auth;

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

    private String thumbnail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private boolean marketingAgree;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean membershipYN;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, auth.getPassword())) {
            throw new IllegalArgumentException("Bad credential");
        }
    }

    public List<GrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority(role.name()))
            .map(GrantedAuthority.class::cast)
            .toList();
    }

    public static User toUser(SignUpUserRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
            .auth(toAuth(request.getLoginId(), request.getPassword(), passwordEncoder))
            .age(request.getAge())
            .birth(request.getBirth())
            .email(request.getEmail())
            .gender(request.getGender())
            .role(request.getRole())
            .membershipYN(request.isMembershipYN())
            .phone(request.getPhone())
            .marketingAgree(request.isMarketingAgree())
            .nickName(request.getNickName())
            .thumbnail(request.getThumbnail())
            .point(0)
            .build();
    }
}

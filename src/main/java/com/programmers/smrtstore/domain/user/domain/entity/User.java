package com.programmers.smrtstore.domain.user.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.INCORRECT_PASSWORD;
import static com.programmers.smrtstore.domain.user.domain.entity.Auth.toAuth;

import com.programmers.smrtstore.domain.user.exception.UserException;
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
    private boolean repurchase;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public User(Auth auth, Integer age, String birth, String email, Gender gender, Role role,
        boolean membershipYN, String phone, boolean marketingAgree,
        String nickName, String thumbnail) {
        this.age = age;
        this.auth = auth;
        this.birth = birth;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.membershipYN = membershipYN;
        this.phone = phone;
        this.marketingAgree = marketingAgree;
        this.nickName = nickName;
        this.thumbnail = thumbnail;
        this.point = 0;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, auth.getPassword())) {
            throw new UserException(INCORRECT_PASSWORD, credentials);
        }
    }

    public List<GrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority(role.name()))
            .map(GrantedAuthority.class::cast)
            .toList();
    }

    public void updateUser(String loginId, String password, Integer age, String nickName,
        String email, String phone,
        String birth, Gender gender, String thumbnail, boolean marketingAgree,
        boolean membershipYN) {
        this.getAuth().updateAuth(loginId, password);
        this.age = age;
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.gender = gender;
        this.thumbnail = thumbnail;
        this.marketingAgree = marketingAgree;
        this.membershipYN = membershipYN;
    }

    public static User toUser(SignUpUserRequest request, PasswordEncoder passwordEncoder) {
        Auth auth = toAuth(request.getLoginId(), request.getPassword(), passwordEncoder);
        return new User(
            auth,
            request.getAge(),
            request.getBirth(),
            request.getEmail(),
            request.getGender(),
            request.getRole(),
            request.isMembershipYN(),
            request.getPhone(),
            request.isMarketingAgree(),
            request.getNickName(),
            request.getThumbnail());
    }
}

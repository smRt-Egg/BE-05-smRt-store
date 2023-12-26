package com.programmers.smrtstore.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "auth_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String loginId;


    @Column(nullable = false)
    private String password;

    public Auth(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public void updateAuth(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public static Auth toAuth(String loginId, String password, PasswordEncoder passwordEncoder) {
        return new Auth(loginId, passwordEncoder.encode(password));
    }
}

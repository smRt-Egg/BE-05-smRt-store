package com.programmers.smrtstore.domain.auth.application.dto.req;

import com.programmers.smrtstore.domain.auth.domain.entity.Auth;
import com.programmers.smrtstore.domain.auth.presentation.dto.req.SignUpAPIRequest;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
public class SignUpRequest {

    String username;
    String password;
    Integer age;
    String nickName;
    String email;
    String phone;
    String birth;
    Gender gender;
    Role role;
    String thumbnail;
    boolean marketingAgree;
    boolean membershipYN;
    boolean repurchaseYN;

    public Auth toAuthEntity(User user, PasswordEncoder passwordEncoder) {
        return Auth.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .user(user)
            .build();
    }

    public User toUserEntity() {
        return User.builder()
            .age(age)
            .birth(birth)
            .email(email)
            .gender(gender)
            .role(role)
            .phone(phone)
            .marketingAgree(marketingAgree)
            .nickName(nickName)
            .thumbnail(thumbnail)
            .build();
    }

    public static SignUpRequest from(SignUpAPIRequest request) {
        return SignUpRequest.builder()
            .username(request.getUsername())
            .password(request.getPassword())
            .age(request.getAge())
            .nickName(request.getNickName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .birth(request.getBirth())
            .gender(request.getGender())
            .role(request.getRole())
            .thumbnail(request.getThumbnail())
            .marketingAgree(request.isMarketingAgree())
            .membershipYN(request.isMembershipYN())
            .repurchaseYN(request.isRepurchaseYN())
            .build();
    }
}

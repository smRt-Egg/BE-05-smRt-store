package com.programmers.smrtstore.domain.user.presentation.dto.req;

import com.programmers.smrtstore.domain.user.domain.entity.Auth;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;

public record SignUpUserRequest(String loginId, String password, Integer age, String nickName, String email, String phone,
                                String birth, Gender gender, Role role, String thumbnail,
                                boolean marketingAgree, boolean membershipYN, Integer point,
                                LocalDateTime updatedTime, LocalDateTime deletedTime, LocalDateTime createdTime) {
    @Builder
    public SignUpUserRequest {}

    public static User toUser(SignUpUserRequest request) {
        Auth auth = Auth.builder()
            .loginId(request.loginId)
            .password(request.password)
            .build();
        return User.builder()
            .auth(auth)
            .age(request.age)
            .birth(request.birth)
            .createdAt(request.createdTime)
            .deletedAt(request.deletedTime)
            .email(request.email)
            .gender(request.gender)
            .point(request.point)
            .role(request.role)
            .updatedAt(request.updatedTime)
            .membershipYN(request.membershipYN)
            .phone(request.phone)
            .marketingAgree(request.marketingAgree)
            .nickName(request.nickName)
            .thumbnail(request.thumbnail)
            .build();
    }
}

package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpUserResponse {

    String loginId;

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

    Integer point;

    LocalDateTime updatedTime;

    LocalDateTime deletedTime;

    LocalDateTime createdTime;

    public static SignUpUserResponse toSignUpUserResponse(User user) {
        return SignUpUserResponse.builder()
            .age(user.getAge())
            .birth(user.getBirth())
            .createdTime(user.getCreatedAt())
            .deletedTime(user.getDeletedAt())
            .email(user.getEmail())
            .gender(user.getGender())
            .loginId(user.getAuth().getLoginId())
            .password(user.getAuth().getPassword())
            .marketingAgree(user.isMarketingAgree())
            .membershipYN(user.isMembershipYN())
            .nickName(user.getNickName())
            .updatedTime(user.getUpdatedAt())
            .point(user.getPoint())
            .role(user.getRole())
            .phone(user.getPhone())
            .thumbnail(user.getThumbnail())
            .build();
    }
}

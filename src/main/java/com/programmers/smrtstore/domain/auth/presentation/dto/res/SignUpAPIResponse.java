package com.programmers.smrtstore.domain.auth.presentation.dto.res;

import com.programmers.smrtstore.domain.auth.application.dto.res.SignUpResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpAPIResponse {

    Long id;
    String username;
    String nickName;
    String email;
    String phone;
    Integer age;
    String birth;
    String gender;
    String thumbnail;
    boolean marketingAgree;
    boolean membershipYN;
    Integer point;
    String createdTime;

    public static SignUpAPIResponse from(SignUpResponse response) {
        return new SignUpAPIResponse(
            response.getId(),
            response.getUsername(),
            response.getNickName(),
            response.getEmail(),
            response.getPhone(),
            response.getAge(),
            response.getBirth(),
            response.getGender().name(),
            response.getThumbnail(),
            response.isMarketingAgree(),
            response.isMembershipYn(),
            response.getPoint(),
            response.getCreatedTime().toString());
    }
}

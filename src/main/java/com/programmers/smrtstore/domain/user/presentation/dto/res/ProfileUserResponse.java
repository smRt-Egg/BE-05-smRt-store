package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.enums.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileUserResponse {

    private Long id;

    private Integer age;

    private String nickName;

    private String email;

    private String phone;

    private String birth;

    private Gender gender;

    private String thumbnail;

    private Boolean marketingAgree;

    public static ProfileUserResponse from(User user) {
        return new ProfileUserResponse(
            user.getId(),
            user.getAge(),
            user.getNickName(),
            user.getEmail(),
            user.getPhone(),
            user.getBirth(),
            user.getGender(),
            user.getThumbnail(),
            user.getMarketingAgree()
        );
    }
}

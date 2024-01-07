package com.programmers.smrtstore.domain.auth.application.dto.res;

import com.programmers.smrtstore.domain.auth.domain.entity.Auth;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpResponse {

    Long id;
    String username;
    String nickName;
    String email;
    String phone;
    Integer age;
    String birth;
    Gender gender;
    String thumbnail;
    boolean marketingAgree;
    boolean membershipYn;
    boolean repurchaseYn;
    Integer point;
    LocalDateTime createdTime;

    public static SignUpResponse from(Auth auth) {
        return new SignUpResponse(
            auth.getUser().getId(),
            auth.getUsername(),
            auth.getUser().getNickName(),
            auth.getUser().getEmail(),
            auth.getUser().getPhone(),
            auth.getUser().getAge(),
            auth.getUser().getBirth(),
            auth.getUser().getGender(),
            auth.getUser().getThumbnail(),
            auth.getUser().isMarketingAgree(),
            auth.getUser().isMembershipYn(),
            auth.getUser().isRepurchaseYn(),
            auth.getUser().getPoint(),
            auth.getUser().getCreatedAt()
        );
    }
}

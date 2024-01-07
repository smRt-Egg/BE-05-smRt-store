package com.programmers.smrtstore.domain.user.presentation.dto.req;

import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @Min(value = 7, message = "나이는 7살 이상이어야 합니다.")
    @Max(value = 200, message = "나이는 200살 이하여야 합니다.")
    Integer age;

    @NotBlank
    @Size(min = 1, max = 10, message = "별명은 1~10자여야 합니다.")
    String nickName;

    @NotBlank @Email
    String email;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    String phone;

    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "올바른 생년월일 형식이 아닙니다.")
    String birth;

    @NotNull
    Gender gender;

    String thumbnail;

    @NotNull
    boolean marketingAgree;

    @NotNull
    boolean membershipYn;
}

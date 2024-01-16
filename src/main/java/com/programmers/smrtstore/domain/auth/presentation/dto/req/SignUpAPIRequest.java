package com.programmers.smrtstore.domain.auth.presentation.dto.req;

import com.programmers.smrtstore.domain.user.domain.enums.Gender;
import com.programmers.smrtstore.domain.user.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpAPIRequest {

    @NotBlank
    @Size(max = 12, min = 4, message = "Login Id는 4~12자리여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login ID는 영문자 또는 숫자만 가능합니다.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?\\\\|]+$",
        message = "비밀번호는 영문자, 숫자, 특수문자만 허용됩니다.")
    private String password;

    @Min(value = 7, message = "나이는 7살 이상이어야 합니다.")
    @Max(value = 200, message = "나이는 200살 이하여야 합니다.")
    private Integer age;

    @NotBlank
    @Size(min = 1, max = 10, message = "별명은 1~10자여야 합니다.")
    private String nickName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phone;

    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "올바른 생년월일 형식이 아닙니다.")
    private String birth;

    @NotNull
    private Gender gender;

    @NotNull
    private Role role;

    private String thumbnail;

    @NotNull
    private boolean marketingAgree;

    @NotNull
    private boolean membershipYN;
}

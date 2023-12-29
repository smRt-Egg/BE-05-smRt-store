package com.programmers.smrtstore.domain.user.presentation.dto.req;

import static com.programmers.smrtstore.domain.user.domain.entity.Auth.toAuth;

import com.programmers.smrtstore.domain.user.domain.entity.Auth;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class SignUpUserRequest {

    @NotBlank
    @Size(max = 12, min = 4, message = "Login Id는 4~12자리여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login ID는 영문자 또는 숫자만 가능합니다.")
    String loginId;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?\\\\|]+$",
        message = "비밀번호는 영문자, 숫자, 특수문자만 허용됩니다.")
    String password;

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

    @NotNull
    Role role;

    String thumbnail;

    @NotNull
    boolean marketingAgree;

    @NotNull
    boolean membershipYN;

    @NotNull
    boolean repurchaseYN;

    public User toUser(PasswordEncoder passwordEncoder) {
        Auth auth = toAuth(loginId, password, passwordEncoder);
        return User.builder()
            .auth(auth)
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
}
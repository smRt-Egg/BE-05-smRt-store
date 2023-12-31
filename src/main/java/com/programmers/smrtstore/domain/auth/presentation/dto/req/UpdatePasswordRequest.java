package com.programmers.smrtstore.domain.auth.presentation.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?\\\\|]+$",
        message = "비밀번호는 영문자, 숫자, 특수문자만 허용됩니다.")
    String password;
}

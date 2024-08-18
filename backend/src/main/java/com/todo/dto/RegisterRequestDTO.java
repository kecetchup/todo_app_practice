package com.todo.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterRequestDTO {
    @NotNull(message = "사용자명은 필수입니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "이메일은 필수입니다.å")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;
}

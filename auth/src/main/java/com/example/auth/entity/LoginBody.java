package com.example.auth.entity;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class LoginBody {
    @NotBlank(message = "名称不能为空")
    private String username;

    private String password;

    @NotBlank(message = "验证码不能为空")

    private String validateCode;

    private String uuid;
}

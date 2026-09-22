package com.demo.demo.controller.dto;

import com.demo.demo.controller.validation.OnLogin;
import com.demo.demo.controller.validation.OnRegistration;
import com.demo.demo.controller.validation.PasswordStrength;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDto {

    @Size(groups = OnRegistration.class, min = 8, message = "회원가입 시 최초 유저 아이디는 8글자 이상이어야합니다")
    @Size(groups = OnLogin.class, min = 4, message = "유저 아이디는 4글자 이상이어야합니다")
    private final String username;
    //  @Size(groups = OnRegistration.class, min = 8, message = "회원가입 시 최초 유저 패스워드는 8글자 이상이어야합니다")
    @PasswordStrength(groups = OnRegistration.class, min = 4, max = 8)
    @Size(groups = OnLogin.class, min = 4, message = "유저 패스워드는 4글자 이상이어야합니다")
    private final String password;
}

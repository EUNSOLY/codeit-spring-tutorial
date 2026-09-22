package com.demo.demo.service;

import com.demo.demo.controller.dto.LoginRequestDto;
import com.demo.demo.exception.ErrorType;
import com.demo.demo.exception.UserException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class LoginService {

    public void connect(
            @Valid LoginRequestDto request
    ) {
        this.connect(request.getUsername(), request.getPassword());
    }

    // 1개 계정밖에 허용되지 않는 데이터베이스 - username: "admin" + password: "1234"
    public void connect(
            @Size(min = 4) String username,
            @Size(min = 4) String password
//            String username,
//            String password
    ) {
//        if (true) {
//            throw new RuntimeException("예상치 못한 갑작스러운 예외/오류 발생");
//        }
        if (!StringUtils.hasLength(username)) {
            throw new UserException(ErrorType.User.USERNAME_NOT_EXIST);
        }
        if (!StringUtils.hasLength(password)) {
            throw new UserException(ErrorType.User.PASSWORD_NOT_EXIST);
        }
        if (!username.equals("admin") || !password.equals("1234")) {
            throw new UserException(ErrorType.User.AUTHENTICATION_FAILED);
        }
        System.out.println("- 데이터베이스 접속 성공");
    }
}
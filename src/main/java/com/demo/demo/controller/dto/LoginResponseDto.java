package com.demo.demo.controller.dto;


import com.demo.demo.exception.ErrorType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {

    private final boolean success;
    private final String username;
    private final String message;

    public static LoginResponseDto success(String username) {
        return new LoginResponseDto(true, username, "로그인이 성공되었습니다.");
    }

    public static LoginResponseDto failed(ErrorType type) {
        return new LoginResponseDto(false, null, type.getMessage());
    }

    public static LoginResponseDto failed(String message) {
        return new LoginResponseDto(false, null, message);
    }

}

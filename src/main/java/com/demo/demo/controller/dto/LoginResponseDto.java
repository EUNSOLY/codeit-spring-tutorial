package com.demo.demo.controller.dto;


import com.demo.demo.exception.ErrorType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto<T> {

    private final boolean success;
    private final String username;
    private final String message;
    private final T detail;

    public static LoginResponseDto<Void> success(String username) {
        return new LoginResponseDto<>(true, username, "로그인이 성공되었습니다.", null);
    }

    public static LoginResponseDto<Void> failed(ErrorType type) {
        return new LoginResponseDto<>(false, null, type.getMessage(), null);
    }

    public static LoginResponseDto<Void> failed(String message) {
        return new LoginResponseDto<>(false, null, message, null);
    }

    public static <T> LoginResponseDto<T> failed(String message, T detail) {
        return new LoginResponseDto<>(false, null, message, detail);
    }
}

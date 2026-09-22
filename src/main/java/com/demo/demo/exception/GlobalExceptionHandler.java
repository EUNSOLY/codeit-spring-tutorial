package com.demo.demo.exception;

import com.demo.demo.controller.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomException.class)
    public LoginResponseDto handle(CustomException exception) {
        log.makeLoggingEventBuilder(exception.getType().getLevel())
                .log(exception.getMessage(), exception);
        return LoginResponseDto.failed(exception.getType());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public LoginResponseDto handle(Exception e) {
        log.error(e.getMessage(), e);
        return LoginResponseDto.failed(e.getMessage());
    }
}

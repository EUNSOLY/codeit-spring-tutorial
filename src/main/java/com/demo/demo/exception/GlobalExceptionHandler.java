package com.demo.demo.exception;

import com.demo.demo.controller.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<LoginResponseDto> handle(CustomException exception) {
        log.makeLoggingEventBuilder(exception.getType().getLevel())
                .log(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(LoginResponseDto.failed(exception.getType()));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LoginResponseDto> handle(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(LoginResponseDto.failed(e.getMessage()));
    }
}

package com.demo.demo.exception.dto;

import com.demo.demo.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ErrorReponse {
    private final ErrorCode errorCode;
    private final String message;
    private final Map<String, Object> context;

}

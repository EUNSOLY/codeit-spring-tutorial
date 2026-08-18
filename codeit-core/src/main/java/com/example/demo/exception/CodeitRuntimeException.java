package com.example.demo.exception;

import lombok.Getter;

@Getter
public class CodeitRuntimeException extends RuntimeException {
    private final ExceptionType exceptionType;

    public CodeitRuntimeException(ExceptionType type) {
        super(type.getMessage());
        this.exceptionType = type;
    }
}

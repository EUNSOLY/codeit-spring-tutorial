package com.example.demo.exception;

import lombok.Getter;

@Getter
public class CodeitRuntimeException extends RuntimeException {
    private final CodeitExceptionType exceptionType;

    public CodeitRuntimeException(CodeitExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }
}

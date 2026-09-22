package com.demo.demo.exception;

public class UserException extends CustomException {
    public UserException(ErrorType type) {
        super(type);
    }
}

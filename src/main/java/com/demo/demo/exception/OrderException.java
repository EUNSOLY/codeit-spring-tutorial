package com.demo.demo.exception;

public class OrderException extends CustomException {
    public OrderException(ErrorType type) {
        super(type);
    }
}

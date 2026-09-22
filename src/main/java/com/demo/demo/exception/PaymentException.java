package com.demo.demo.exception;

public class PaymentException extends CustomException {
    public PaymentException(ErrorType type) {
        super(type);
    }
}

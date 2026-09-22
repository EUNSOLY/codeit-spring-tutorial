package com.demo.demo.order.exception;

import com.demo.demo.exception.ApplicationException;
import com.demo.demo.exception.ErrorCode;

/**
 * 주문 도메인 관련 예외의 기본 클래스
 */
public abstract class OrderException extends ApplicationException {

    protected OrderException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected OrderException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}

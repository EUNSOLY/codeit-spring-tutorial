package com.demo.demo.user.exception;

import com.demo.demo.exception.ApplicationException;
import com.demo.demo.exception.ErrorCode;

/**
 * 사용자 도메인 관련 예외의 기본 클래스
 * 사용자 관련 모든 예외는 이 클래스를 상속한다
 */
public abstract class UserException extends ApplicationException {

    protected UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected UserException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}

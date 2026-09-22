package com.demo.demo.user.exception;

import com.demo.demo.exception.ErrorCode;


/**
 * 중복된 이메일 주소로 회원가입을 시도할 때 발생하는 예외
 */
public class DuplicateEmailException extends UserException {

    public DuplicateEmailException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, String.format("이미 사용 중인 이메일 주소입니다: %s", email));
        super.addContext("email", email);
        super.addContext("domain", "user-registration");
    }
}

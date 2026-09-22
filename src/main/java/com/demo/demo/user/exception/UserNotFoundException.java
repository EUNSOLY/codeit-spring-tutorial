package com.demo.demo.user.exception;

import com.demo.demo.exception.ErrorCode;


/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
public class UserNotFoundException extends UserException {

    public UserNotFoundException(Long userId) {
        super(ErrorCode.DUPLICATE_EMAIL, String.format("사용자를 찾을 수 없습니다: ID=%d", userId));
        super.addContext("userId", userId);
        super.addContext("searchType", "byId");
    }

    public UserNotFoundException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, String.format("사용자를 찾을 수 없습니다: Email=%s", email));
        super.addContext("email", email);
        super.addContext("searchType", "byEmail");
    }
}

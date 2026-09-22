package com.demo.demo.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 일반적인 오류 (4xx)
    INVALID_INPUT(400, "COMMON_001", "입력값이 올바르지 않습니다"),
    RESOURCE_NOT_FOUND(404, "COMMON_002", "요청한 리소스를 찾을 수 없습니다"),
    ACCESS_DENIED(403, "COMMON_003", "접근 권한이 없습니다"),

    // 사용자 관련 오류 (4xx)
    USER_NOT_FOUND(404, "USER_001", "사용자를 찾을 수 없습니다"),
    DUPLICATE_EMAIL(400, "USER_002", "이미 사용 중인 이메일 주소입니다"),
    INVALID_PASSWORD(400, "USER_003", "비밀번호가 정책에 맞지 않습니다"),

    // 주문 관련 오류 (4xx)
    INSUFFICIENT_STOCK(400, "ORDER_001", "재고가 부족합니다"),
    INVALID_ORDER_STATUS(400, "ORDER_002", "주문 상태가 올바르지 않습니다"),

    // 시스템 오류 (5xx)
    DATABASE_ERROR(500, "SYS_001", "데이터베이스 오류가 발생했습니다"),
    EXTERNAL_API_ERROR(500, "SYS_002", "외부 API 호출에 실패했습니다"),
    INTERNAL_SERVER_ERROR(500, "SYS_999", "내부 서버 오류가 발생했습니다");

    private final int httpStatus;
    private final String code;
    private final String defaultMessage;
}

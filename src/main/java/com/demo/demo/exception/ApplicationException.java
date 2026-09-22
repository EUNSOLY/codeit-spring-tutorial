package com.demo.demo.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;              // 구체적인 오류 코드
    private final Map<String, Object> context;      // 예외 발생 맥락 정보
    private final Instant timestamp;                // 예외 발생 시각


    protected ApplicationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.context = new HashMap<>();
        this.timestamp = Instant.now();
    }

    protected ApplicationException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.context = new HashMap<>();
        this.timestamp = Instant.now();
    }

    /**
     * 예외 발생 맥락 정보를 추가한다.
     * 디버깅과 로깅에 유용한 정보를 포함할 수 있다.
     */
    public ApplicationException addContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }

    // Getter : 나머지는 Lombok
    public Map<String, Object> getContext() {
        return Collections.unmodifiableMap(context); // unmodifiableMap : 읽기 전용 맵
    }
}

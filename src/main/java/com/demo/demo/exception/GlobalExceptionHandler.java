package com.demo.demo.exception;

import com.demo.demo.exception.dto.ErrorReponse;
import com.demo.demo.order.exception.InsufficientStockException;
import com.demo.demo.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예외 계층 구조를 활용한 통합 예외 처리 예시
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 사용자 도메인 예외를 처리한다.
     * 구체적인 예외 타입에 관계없이 공통 처리 로직을 적용한다.
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorReponse> handle(UserException e) {

        // 도메인별 공통 로깅
        log.warn("사용자 도메인 예외 발생 : {} - {}", e.getErrorCode().getCode(), e.getMessage());

        // 컨텍스트 정보 로깅
        e.getContext().forEach((key, value) -> log.debug("Context: {} = {}", key, value));

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ErrorReponse(e.getErrorCode(), e.getMessage(), e.getContext()));
    }

    /**
     * 특정 예외에 대한 세밀한 처리
     * 재고 부족 예외의 경우 추가적인 정보를 제공한다
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorReponse> handle(InsufficientStockException e) {

        // 비즈니스 메트릭 업데이트
//        metricsService.incrementCounter("order.stock.insufficient",
//                Tags.of("productId", e.getContext().get("productId").toString()));

        // 재고 알림 서비스 호출
//        stockNotificationService.notifyLowStock(
//                (Long) e.getContext().get("productId"),
//                (Integer) e.getContext().get("availableQuantity")
//        );
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(createEnhancedErrorResponse(e));
    }


    private ErrorReponse createEnhancedErrorResponse(InsufficientStockException e) {
        ErrorReponse response = new ErrorReponse(e.getErrorCode(), e.getMessage(), e.getContext());

        // 사용자에게 도움이 되는 추가 정보 제공
//        response.addSuggestion("다른 상품을 확인해보세요");
//        response.addSuggestion("재고 알림을 설정하실 수 있습니다");

        return response;
    }
}

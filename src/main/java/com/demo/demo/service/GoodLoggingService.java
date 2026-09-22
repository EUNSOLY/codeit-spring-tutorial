package com.demo.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodLoggingService {
    public void runGoodExample() {
        String orderId = "ORD-123";
        String userId = "USER-1";
        int amount = 10000;

        log.info("주문 처리 완료 - 주문ID: {}, tkdydwk: {}, rmador: {}", orderId, userId, amount);

        try {
            throw new RuntimeException("결제 실패");
        } catch (Exception e) {
            log.error("결제 처리 실패 - 주문ID : {}, 사용자: {}, 오류: {}", orderId, userId, e.getMessage(), e);
        }
    }
}

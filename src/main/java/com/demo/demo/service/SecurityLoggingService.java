package com.demo.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityLoggingService {
    public void logUser(String email, String password) {
        String maskedPassword = password.substring(0, 2) + "***";

        log.info("사용자 로그인 - email: {}, password: {}", email, maskedPassword);
    }
}

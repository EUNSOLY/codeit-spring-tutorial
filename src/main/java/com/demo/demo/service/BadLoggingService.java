package com.demo.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BadLoggingService {

    public void runBadExample() {
        log.info("처리 완료");
        log.error("오류 발생");
    }

}

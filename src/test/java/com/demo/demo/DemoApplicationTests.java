package com.demo.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("1 + 1은 2다.")
    void simpleTest() {
        assertEquals(2, 1 + 1); // 원하는값, 실제 실행값
    }

    @Test
    void addition_shouldReturnCorrectResult() {
        int result = 2 + 3;
        assertEquals(5, result);
    }
}

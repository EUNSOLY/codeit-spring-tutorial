package com.demo.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // 서비스 인스턴스 생성
        System.out.println("BeforeEach 실행");
        userService = new UserService();
    }

    @AfterEach
    void tearDown() {
        // 로그 또는 자워 해지 처리
        System.out.println("AfterEach 실행");
    }

    @Test
    void createUser_shouldReturnName() {
        String result = userService.create("kim");
        assertEquals("kim", result);
        System.out.println("createUser_shouldReturnName 실행");
    }

    @Test
    void createUser_shouldNotReturnNull() {
        String result = userService.create("lee");

        assertNotNull(result);
        System.out.println("createUser_shouldNotReturnNull 실행");
    }

}

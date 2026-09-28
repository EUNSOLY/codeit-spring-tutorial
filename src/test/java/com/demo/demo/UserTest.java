package com.demo.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private UserService userService;


    @BeforeEach
    void setUp() {
        // 서비스 인스턴스 생성
        System.out.println("BeforeEach 실행");
        userService = new UserService();
    }

    @Test
    @DisplayName("이메일과 이름으로 User를 생성하면 값이 올바르게 저장된다")
    void userValidation() {
        User user = new User("kim@example.com", "kim");
        assertAll("사용자 검증",
                () -> assertEquals("kim", user.getName()),
                () -> assertTrue(user.getEmail().contains("@")),
                () -> assertNotNull(user)
        );

        System.out.println("User Test - userValidation 실행");
    }

    @Test
    void groupAssertions() {
        User user = new User("alice@example.com", "Alice");

        assertAll(
                "User 정보 검증",
                () -> assertEquals("Alice", user.getName()),
                () -> assertTrue(user.getEmail().contains("@")),
                () -> assertFalse(user.isDeleted())
        );
    }

    @Test
    void shouldThrowIllegalArgumentException() {

        // 실패 테스트 : 예외가 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            userService.create(null);
        });

        // assertDoesNotThrow(executable) : 예외가 하나라도 터지면 테스트 실패(즉 성공 케이스만 넣어봐서 테스트해야함)


        System.out.println("shouldThrowIllegalArgumentException 통과");
    }


}

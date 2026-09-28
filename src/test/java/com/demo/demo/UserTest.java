package com.demo.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {


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


}

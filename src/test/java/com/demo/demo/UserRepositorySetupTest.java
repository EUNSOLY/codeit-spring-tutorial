package com.demo.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositorySetupTest {

    @Autowired
    private UserRepository userRepository;

    // 테스트에 사용할 샘플 데이터 준비
    @BeforeEach
    void setUp() {
        userRepository.save(User.builder().email("a@example.com").name("Alice").status("ONLINE").build());
        userRepository.save(User.builder().email("bob@example.com").name("bob").status("ONLINE").build());
    }

    @Test
    @DisplayName("기본 유저가 2명 존재해야한다.")
    void givenSetup_whenCountUsers_thenReturns2() {
        // When
        long count = userRepository.count();

        assertEquals(2, count);

    }
}

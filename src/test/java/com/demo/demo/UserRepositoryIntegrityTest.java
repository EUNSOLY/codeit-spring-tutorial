package com.demo.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:54322/menudb",
        "spring.datasource.username=root",
        "spring.datasource.password=root",
        "spring.jpa.hibernate.ddl-auto=create"
})
public class UserRepositoryIntegrityTest {
    // Given - 준비: 테스트에 필요한 데이터나 상태를 만듬
    // When - 실행: 테스트하려는 동작을 딱 한번 한다.
    // Then - 검증: 결과가 기대한 대로인지 확인

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일이 NULL이면 저장 실패해야 한다")
    void nullEmail_shouldFail() {
        // Given
        User user = User.builder()
                .email(null)
                .name("Test")
                .status("ACTIVE")
                .build();

        // When & Then
        DataIntegrityViolationException exception =
                assertThrows(DataIntegrityViolationException.class,
                        () -> userRepository.saveAndFlush(user)
                );

        // Them (예외 메시지 출력)
        System.out.println("=== NOT NULL 예외 메시지 ===");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("이메일이 중복되면 저장 실패해야 한다")
    void duplicateEmail_shouldFail() {

        // Given
        userRepository.saveAndFlush(
                User.builder()
                        .email("dup@test.com")
                        .name("A")
                        .status("ACTIVE")
                        .build()
        );

        User duplicate = User.builder()
                .email("dup@test.com")
                .name("B")
                .status("ACTIVE")
                .build();

        // When & Then
        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(duplicate)
        );

    }
}

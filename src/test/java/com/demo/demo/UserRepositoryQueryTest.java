package com.demo.demo;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:54322/menudb",
        "spring.datasource.username=root",
        "spring.datasource.password=root",
        "spring.jpa.hibernate.ddl-auto=none"
})
public class UserRepositoryQueryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("@Query로 이메일 조회")
    void queryTest() {

        // Given
        userRepository.save(
                User.builder()
                        .email("query@test.com")
                        .name("Query")
                        .status("ACTIVE")
                        .build()
        );

        // When
        Optional<User> result = userRepository.findByEmailWithQuery("query@test.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Query", result.get().getName());

    }

}

package com.demo.demo;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
        properties = {
                // 1. 접속 URL
                "spring.datasource.url=jdbc:postgresql://localhost:5432/testdb",
                // 2. JDBC 드라이버
                "spring.datasource.driver-class-name=org.postgresql.Driver",
                // 3. Username
                "spring.datasource.username=postgres",
                // 4. Password
                "spring.datasource.password=yourpassword",
                // 5. SQL 방언
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"
        }
)
public class UserRepositoryRealDbTest {
}

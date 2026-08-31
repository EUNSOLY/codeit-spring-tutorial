package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.time.ZoneId;

@Slf4j
@Repository
public class UserJdbcRepository {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public User findById(int userId) throws SQLException {
        Connection connection = null; // 1.
        Statement statement = null;   // 2.
        ResultSet resultSet = null;   // 3.

        try {
            connection = DriverManager.getConnection(url, username, password); // // 1. DB에 연결해서 Connection 객체 생성
            statement = connection.createStatement(); // 2. SQL을 실행할 Statement 객체 생성
            resultSet = statement.executeQuery("SELECT * FROM \"user\"WHERE id = " + userId); // 3. SQL 실행 후 결과를 ResultSet에 저장

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                );
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다 - id : " + userId);

        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            // 자원반납
            if (resultSet != null) resultSet.close();   // 1.
            if (statement != null) statement.close();   // 2.
            if (connection != null) connection.close(); // 3.
        }

    }
}

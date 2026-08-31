package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserJdbcRepository {
    private final DataSource dataSource;

    public User findById(int userId) throws SQLException {
        Connection connection = null; // 1.
        PreparedStatement statement = null;   // 2.
        ResultSet resultSet = null;   // 3.

        try {
            connection = dataSource.getConnection(); // 1. Hikari 커넥션 풀에서 Connection 객체 하나를 꺼내옴
            statement = connection.prepareStatement("SELECT * FROM \"user\" WHERE id = ?"); // 2. SQL을 실행할 Statement 객체 생성
            statement.setInt(1, userId); // 3. SQL 실행
            resultSet = statement.executeQuery();// 3. 결과를 ResultSet에 저장


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

    public List<User> findAll() throws SQLException {
        Connection connection = null; // 1.
        PreparedStatement statement = null;   // 2.
        ResultSet resultSet = null;   // 3.

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM \"user\"");
            resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        new User(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("age"),
                                resultSet.getString("job"),
                                resultSet.getString("specialty"),
                                resultSet.getTimestamp("created_at")
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()
                        )
                );
            }

            return users;
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}

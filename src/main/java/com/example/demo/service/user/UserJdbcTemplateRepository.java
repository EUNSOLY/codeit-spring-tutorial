package com.example.demo.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public User findById(int userId) throws SQLException {
        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        int getUserParams = userId;

        return this.jdbcTemplate.queryForObject(
                getUserQuery,
                (resultSet, rowNum) -> new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("job"),
                        resultSet.getString("specialty"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getUserParams
        );
    }

    public List<User> findAll() throws SQLException {
        return null;
    }

    public User save(String name, Integer age, String job, String specialty) throws SQLException {
        return null;
    }
}

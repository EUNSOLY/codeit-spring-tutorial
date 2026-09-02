package com.example.demo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageJdbcTemplateRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public List<Message> findByUserId(Integer userId) throws SQLException {
        Integer parameter = userId;
        return jdbcTemplate.queryForStream(
                "SELECT * FROM \"message\" WHERE user_id = ?",
                (resultSet, rowNum) -> new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                parameter
        ).toList();
    }

    public Message create(Integer userId, String message) {
        Object[] paramters = new Object[]{
                userId,
                message,
                LocalDateTime.now()
        };

        jdbcTemplate.update(
                "INSERT INTO \"message\" (user_id, message, created_at) VALUES (?,?,?)",
                paramters
        );

        Integer createdMessageId = jdbcTemplate.queryForObject(
                "SELECT lastval()",
                Integer.class
        );

        Integer parameter = createdMessageId;
        return jdbcTemplate.queryForObject(
                "SELECT * FROM \"message\" WHERE id = ?",
                (resultSet, rowNum) -> new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                parameter
        );
    }
}

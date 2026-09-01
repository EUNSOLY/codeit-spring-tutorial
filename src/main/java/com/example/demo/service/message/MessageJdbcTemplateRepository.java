package com.example.demo.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Message> findByUserId(int userId) {
        String getUserMessageQuery = "SELECT * FROM  \"message\" WHERE user_id = ?";
        int getMessageParams = userId;

        return this.jdbcTemplate.queryForStream(
                getUserMessageQuery,
                (resultSet, rowNum) -> new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                getMessageParams
        ).toList();
    }

    public List<Message> save(Integer userId, String message) {
        String createMessageQuery = "INSERT INTO \"message\" (user_id, message, created_at) VALUES(?, ?, ?)";
        Object[] createMessageParams = new Object[]{userId, message, LocalDateTime.now()};
        this.jdbcTemplate.update(
                createMessageQuery,
                createMessageParams
        );
        String lastInsertIdQuery = "SELECT lastval()"; // 마지막 저장 데이터 ID
        Integer createdMessageId = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

        String getMessageQuery = "SELECT * FROM  \"message\" WHERE id = ?";

        return this.jdbcTemplate.queryForStream(
                getMessageQuery,
                (resultSet, rowNum) -> new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ),
                createdMessageId
        ).toList();
    }
}

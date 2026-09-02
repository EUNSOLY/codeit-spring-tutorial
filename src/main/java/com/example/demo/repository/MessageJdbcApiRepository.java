package com.example.demo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageJdbcApiRepository {
    private final DataSource dataSource;

    public List<Message> findByUserId(Integer userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM \"message\" WHERE user_id = ?");
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                ));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != resultSet) resultSet.close();
            if (null != statement) statement.close();
            if (null != connection) connection.close();
        }
    }

    public Message create(Integer userId, String message) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO \"message\" (user_id, message, created_at) VALUES (?,?,?);");
            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();

            // 방금 저장한 MessageId
            Integer createdMessageId = null;
            statement = connection.prepareStatement("SELECT lastval();");
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                createdMessageId = resultSet.getInt("lastval");
            }

            // SELECT 메세지 정보
            statement = connection.prepareStatement("SELECT * FROM \"message\" WHERE id = ?");
            
            statement.setInt(1, createdMessageId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != resultSet) resultSet.close();
            if (null != statement) statement.close();
            if (null != connection) connection.close();
        }

    }
}

package com.example.demo.service.message;

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
public class MessageJdbRepository {
    private final DataSource dataSource;

    public List<Message> findByUserId(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM  \"message\" WHERE user_id = ?");
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
                        )
                );
            }
            return messages;

        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
            if (resultSet != null) resultSet.close();
        }
    }

    public Message save(Integer userId, String message) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("INSERT INTO \"message\" (user_id, message) VALUES(?,?)");
            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.executeUpdate(); // 업데이트

            statement = connection.prepareStatement("SELECT lastval()");
            resultSet = statement.executeQuery();

            Integer createMessageId = null;
            if (resultSet.next()) {
                createMessageId = resultSet.getInt("id");
                statement = connection.prepareStatement("SELECT * FROM \"message\" WHERE id = ?");
                statement.setInt(1, createMessageId);
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

            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메세지가 저장되지 않았습니다 - id : " + createMessageId);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원에 대한 접근에 문제가 있습니다.");
        } finally {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
            if (resultSet != null) resultSet.close();
        }
    }
}

package com.example.demo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        resultSet.getTimestamp("create_at")
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

}

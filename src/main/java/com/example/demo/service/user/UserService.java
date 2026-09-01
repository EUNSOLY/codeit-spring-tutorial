package com.example.demo.service.user;

import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.service.message.Message;
import com.example.demo.service.message.MessageJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DataSource dataSource;
    private final UserJdbcRepository userJdbcRepository;
    private final MessageJdbcRepository messageJdbcRepository;
    private final UserJdbcTemplateRepository userJdbcTemplateRepository;


    public UserResponseDto findById(Integer id) {
        User user = userJdbcTemplateRepository.findById(id);
        return UserResponseDto.from(user);
    }

    public List<UserResponseDto> findAll() {
        return userJdbcTemplateRepository.findAll()
                .stream()
                .map(UserResponseDto::from)
                .toList();
    }

    public UserResponseDto save(String name, Integer age, String job, String specialty) throws SQLException {
        Connection connection = null;
        try {
            TransactionSynchronizationManager.initSynchronization();
            connection = dataSource.getConnection();    // Connection 생성
            connection.setAutoCommit(false);            // Connection Auto-Commit 옵션 끄기
            TransactionSynchronizationManager.bindResource(dataSource, new ConnectionHolder(connection));

            User user = userJdbcRepository.save(name, age, job, specialty);
            List<Message> messages = messageJdbcRepository.save(user.getId(), user.getName() + "님 가입을 환영합니다.");

            connection.commit();                        // (A) Commit

            UserResponseDto userResponse = UserResponseDto.from(user);
            userResponse.setMessage(messages);

            return userResponse;
        } catch (SQLException e) {
            connection.rollback();                  // (B) Rollback
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원 반납 시 문제가 있습니다.");
        } finally {
            TransactionSynchronizationManager.unbindResource(dataSource);
            connection.setAutoCommit(true);
            connection.close();                     // (C) Close
            TransactionSynchronizationManager.clearSynchronization();
        }
    }
}

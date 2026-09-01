package com.example.demo.service.user;

import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.service.message.Message;
import com.example.demo.service.message.MessageJdbcTemplateRepository;
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
    private final UserJdbcTemplateRepository userJdbcTemplateRepository;
    private final MessageJdbcTemplateRepository messageJdbcTemplateRepository;


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

    public UserResponseDto save(String name, Integer age, String job, String specialty) {
        Connection connection = null;
        try {
            TransactionSynchronizationManager.initSynchronization();
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            TransactionSynchronizationManager.bindResource(dataSource, new ConnectionHolder(connection));

            User user = userJdbcTemplateRepository.save(name, age, job, specialty);
            List<Message> messages = messageJdbcTemplateRepository.save(user.getId(), user.getName() + "님 가입을 환영합니다.");

            connection.commit();

            UserResponseDto userResponse = UserResponseDto.from(user);
            userResponse.setMessage(messages);

            return userResponse;
        } catch (SQLException e) {
            try {
                connection.rollback();                  // (B) Rollback
            } catch (final SQLException ignored) {
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원 반납 시 문제가 있습니다.");
        } catch (Exception e) {
            try {
                connection.rollback();                  // (B) Rollback
            } catch (final SQLException ignored) {
            }
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "자원 반납 시 문제가 있습니다.", e);
        } finally {
            try {
                TransactionSynchronizationManager.unbindResource(dataSource);
                connection.setAutoCommit(true);
                connection.close();                     // (C) Close
                TransactionSynchronizationManager.clearSynchronization();
            } catch (final SQLException ignored) {
            }
        }
    }

    public void delete(Integer id) {
        userJdbcTemplateRepository.delete(id);
        messageJdbcTemplateRepository.deleteByUserId(id);
    }
}

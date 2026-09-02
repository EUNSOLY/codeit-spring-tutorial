package com.example.demo.service;

import com.example.demo.controller.dto.UserCreateRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJdbcApiRepository userJdbcApiRepository;
    private final MessageJdbcApiRepository messageJdbcApiRepository;
    private final UserJdbcTemplateRepository userJdbcTemplateRepository;
    private final MessageJdbcTemplateRepository messageJdbcTemplateRepository;


    public UserResponseDto findById(@NonNull Integer id) throws SQLException {
        User retrievedUser = userJdbcTemplateRepository.findById(id);
        List<Message> messages = messageJdbcTemplateRepository.findByUserId(id);
        return UserResponseDto.from(retrievedUser, messages);
    }

    @Transactional(
//          propagation   = Propagation.REQUIRED,      // Propagation 전파
//          isolation     = Isolation.REPEATABLE_READ, // Isolation Level 격리성 레벨
//          timeout       = -1,                        // Timeout 트랜잭션 타임아웃
//          readOnly      = false,                     // ReadOnly R 만 허용, CUD 방지
//          rollbackFor   = Exception.class,
//          noRollbackFor = RuntimeException.class
    )
    public UserResponseDto create(UserCreateRequestDto request) {
        User createdUser = userJdbcTemplateRepository.create(request.getName(), request.getAge(), request.getJob(), request.getSpecialty());
        Message createdMessages = messageJdbcTemplateRepository.create(createdUser.getId(), createdUser.getName() + "님 회원가입 감사드립니다!");
        return UserResponseDto.from(createdUser, Collections.singletonList(createdMessages));
    }
}

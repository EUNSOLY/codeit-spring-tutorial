package com.example.demo.service;

import com.example.demo.controller.dto.UserCreateRequestDto;
import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.repository.Message;
import com.example.demo.repository.MessageJdbcApiRepository;
import com.example.demo.repository.User;
import com.example.demo.repository.UserJdbcApiRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJdbcApiRepository userJdbcApiRepository;
    private final MessageJdbcApiRepository messageJdbcApiRepository;
    private final PlatformTransactionManager platformTransactionManager; // 트랜잭션 매니저 (DI로 주입됨) - 내부적으로 dataSource 통해 Connection 관리 (내가 등록한 @Bean dataSource 사용)

    public UserResponseDto findById(@NonNull Integer id) throws SQLException {
        User retrievedUser = userJdbcApiRepository.findById(id);
        List<Message> messages = messageJdbcApiRepository.findByUserId(id);
        return UserResponseDto.from(retrievedUser, messages);
    }

    public UserResponseDto create(UserCreateRequestDto request) throws SQLException {
        // 트랜잭션 시작(사전준비) : Connection 대여 > 자동 커밋 끄기(OFF) > TransactionSynchronizationManager로 ThreadLocal에 Connection 저장
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(
                new DefaultTransactionDefinition() // 격리 수준/전파 방식 등 기본 트랜잭션 속성
        );

        try {
            User createdUser = userJdbcApiRepository.create(request.getName(), request.getAge(), request.getJob(), request.getSpecialty());
            Message createdMessages = messageJdbcApiRepository.create(createdUser.getId(), createdUser.getName() + "님 회원가입 감사드립니다!");
            UserResponseDto response = UserResponseDto.from(createdUser, Collections.singletonList(createdMessages));

            // 트랜잭션 종료(사후정리-성공) : 실제 COMMIT > ThreadLocal에서 Connection 삭제 > 자동 커밋 켜기(ON) > Connection 반환
            platformTransactionManager.commit(transactionStatus); // * 자동 커밋이 꺼져있기때문에(OFF) 임시 저장소에 쌓여있는 그동안의 쿼리 결과들을 수동 커밋 COMMIT 통해 단 한방에 데이터베이스에 그 모든것들을 최종 반영해야한다

            return response;
        } catch (SQLException e) {
            // 트랜잭션 종료(사후정리-실패) : 실제 ROLLBACK > ThreadLocal에서 Connection 삭제 > 자동 커밋 켜기(ON) > Connection 반환
            platformTransactionManager.rollback(transactionStatus); // * 자동 커밋이 꺼져있기때문에(OFF) 임시 저장소에 쌓여있는 그동안의 쿼리 결과들을 ROLLBACK 통해 단 한방에 날려버릴 수 있다
            throw new RuntimeException(e);
        }
    }
}

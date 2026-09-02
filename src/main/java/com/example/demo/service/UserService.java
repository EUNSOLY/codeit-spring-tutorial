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
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJdbcApiRepository userJdbcApiRepository;
    private final MessageJdbcApiRepository messageJdbcApiRepository;
    private final TransactionTemplate transactionTemplate;


    public UserResponseDto findById(@NonNull Integer id) throws SQLException {
        User retrievedUser = userJdbcApiRepository.findById(id);
        List<Message> messages = messageJdbcApiRepository.findByUserId(id);
        return UserResponseDto.from(retrievedUser, messages);
    }

    public UserResponseDto create(UserCreateRequestDto request) {
//      transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); // 전파 방식 설정 - 기본값이라 생략 가능. 기존 트랜잭션 있으면 참여, 없으면 새로 시작
//      transactionTemplate.setTimeout(-1); // 트랜잭션 제한시간(초) - 기본값 -1은 제한 없음
//      transactionTemplate.setReadOnly(false); // 읽기 전용 여부 - 기본값 false (조회 전용 트랜잭션이면 true로 최적화 가능)

        UserResponseDto response = transactionTemplate.execute((status) -> {
            // execute : 트랜잭션 시작(getTransaction) + 콜백(람다) 실행 + 성공 시 commit / 예외 발생 시 rollback 을 대신 해줌
            // 즉 이전엔 개발자가 직접 호출하던 getTransaction/commit/rollback을 TransactionTemplate이 알아서 처리
            // status : TransactionStatus - 현재 트랜잭션의 상태 정보를 담고 있는 객체 (지금은 사용 안 함)
            User createdUser = userJdbcApiRepository.create(request.getName(), request.getAge(), request.getJob(), request.getSpecialty());
            Message createdMessages = messageJdbcApiRepository.create(createdUser.getId(), createdUser.getName() + "님 회원가입 감사드립니다!");
            return UserResponseDto.from(createdUser, Collections.singletonList(createdMessages));
        });
        return response;
    }
}

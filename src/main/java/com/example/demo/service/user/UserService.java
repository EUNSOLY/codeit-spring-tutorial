package com.example.demo.service.user;

import com.example.demo.controller.dto.UserResponseDto;
import com.example.demo.service.message.Message;
import com.example.demo.service.message.MessageJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
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
        User user = userJdbcTemplateRepository.save(name, age, job, specialty);
        List<Message> messages = messageJdbcTemplateRepository.save(user.getId(), user.getName() + "님 가입을 환영합니다.");

        UserResponseDto userResponse = UserResponseDto.from(user);
        userResponse.setMessage(messages);

        return userResponse;
    }
}

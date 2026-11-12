package com.demo.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.demo.controller.dto.UserCreateRequestDto;
import com.demo.demo.controller.dto.UserResponseDto;
import com.demo.demo.repository.UserRepository;
import com.demo.demo.repository.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto findById(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("유저가 데이터베이스 내 존재하지 않습니다. 유저 id : " + id));
        return UserResponseDto.from(user);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
            .stream()
            .map(UserResponseDto::from)
            .toList();
    }

    public UserResponseDto save(UserCreateRequestDto request) {
        User user = User.create(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getAge(),
            request.getJob(),
            request.getSpecialty()
        );
        User created = userRepository.save(user);
        return UserResponseDto.from(created);
    }

    public void delete(Integer id) {
        userRepository.delete(id);
    }
}
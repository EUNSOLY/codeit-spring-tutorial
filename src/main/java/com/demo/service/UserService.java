package com.demo.service;

import com.demo.dto.UserCreateRequestDto;
import com.demo.dto.UserResponseDto;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto findById(Integer id) {
        User user = userRepository.read(id);
        if (Objects.isNull(user)) {
            throw new RuntimeException("찾으시는 유저가 없습니다.");
        }

        return UserResponseDto.from(user);
    }


    public List<UserResponseDto> readAll() {
        List<User> users = userRepository.readAll();
        return users.stream().map(UserResponseDto::from).toList();
    }


    public UserResponseDto create(UserCreateRequestDto requestDto) {
        User convetUser = requestDto.toEntity();
        User user = userRepository.create(convetUser);
        return UserResponseDto.from(user);
    }


    public void update(UserCreateRequestDto requestDto) {

    }

    public void delete(Integer id) {
        this.findById(id);
        userRepository.delete(id);
    }
}

package com.demo.controller;

import com.demo.dto.UserCreateRequestDto;
import com.demo.dto.UserResponseDto;
import com.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor // Ioc 제어의 역전을 위해 (final 필드 생성자 자동 생성 → DI(생성자 주입)에 사용)
@RequestMapping(value = "/api/users") // 기본경로
public class UserController {
    private final UserService userService;

    // 회원 전체 조회
    @GetMapping(value = "")
    public List<UserResponseDto> readAll() {
        return userService.readAll();
    }

    @GetMapping(value = "/{id}")
    public UserResponseDto findById(
            @PathVariable Integer id
    ) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 회원 생성
    @PostMapping(value = "")
    public UserResponseDto createUser(
            @RequestBody UserCreateRequestDto requestDto
    ) {
        try {
            return userService.create(requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 회원 수정
    // 회원 삭제
    @DeleteMapping(value = "/{id}")
    public void delete(
            @PathVariable Integer id
    ) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


package com.demo.dto;

import com.demo.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserResponseDto {
    Integer id;
    String name;
    int age;
    String job;
    String specialty;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getAge(), user.getJob(), user.getSpecialty());
    }
}

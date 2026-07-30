package com.demo.dto;


import com.demo.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserCreateRequestDto {
    String name;
    int age;
    String job;
    String specialty;
    
    public User toEntity() {
        return new User(0, this.name, this.age, this.job, this.specialty);
    }
}

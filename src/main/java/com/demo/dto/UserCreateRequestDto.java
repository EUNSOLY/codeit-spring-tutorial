package com.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserCreateRequestDto {
    private String name;
    private Integer age;
    private String job;
    private String specialty;
}
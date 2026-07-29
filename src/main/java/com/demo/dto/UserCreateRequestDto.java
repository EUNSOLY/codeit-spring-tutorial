package com.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequestDto {
    private String name;
    private Integer age;
    private String job;
    private String specialty = "(empty)";
}
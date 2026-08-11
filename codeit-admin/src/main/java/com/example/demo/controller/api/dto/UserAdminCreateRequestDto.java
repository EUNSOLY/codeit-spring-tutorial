package com.example.demo.controller.api.dto;

import com.example.demo.repository.user.User;

public class UserAdminCreateRequestDto extends RequestingUserDto {
    private final String name;

    public UserAdminCreateRequestDto(String name, Integer requestUserId) {
        super(requestUserId);
        this.name = name;
    }

    public User toEntity() {
        return User.create(this.name, super.requestUserId);
    }
}

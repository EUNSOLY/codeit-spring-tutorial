package com.demo.controller.admin.dto;

import com.demo.controller.internal.dto.RequestingUserDto;
import com.demo.domain.user.User;

public class UserAdminCreateRequestDto extends RequestingUserDto {
    private final String name;

    public UserAdminCreateRequestDto(String name, Integer requestUserId) {
        super(requestUserId);
        this.name = name;
    }

    public User to() {
        return User.create(this.name, super.requestUserId);
    }
}

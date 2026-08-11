package com.demo.controller.admin.dto;

import com.demo.controller.internal.dto.RequestingUserDto;
import com.demo.domain.user.UserGrade;
import lombok.Getter;

@Getter
public class UserAdminUpdateRequestDto extends RequestingUserDto {
    private final String name;
    private final UserGrade grade;
    private final int point;

    public UserAdminUpdateRequestDto(String name, UserGrade grade, int point, Integer requestUserId) {
        super(requestUserId);
        this.name = name;
        this.grade = grade;
        this.point = point;
    }
}

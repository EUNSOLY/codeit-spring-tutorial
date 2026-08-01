package com.demo.dto;

import com.demo.entity.Member;
import com.demo.enums.JobType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberResponseDto {
    Integer id;
    String name;
    Integer age;
    JobType job;
    String email;
    boolean isDeleted;

    public static MemberResponseDto to(Member entity) {
        return new MemberResponseDto(entity.getId(), entity.getName(), entity.getAge(), entity.getJob(), entity.getEmail(), entity.isDeleted());
    }
}

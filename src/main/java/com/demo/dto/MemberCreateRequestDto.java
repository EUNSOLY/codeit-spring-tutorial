package com.demo.dto;

import com.demo.entity.Member;
import com.demo.enums.JobType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberCreateRequestDto {
    String name;
    Integer age;
    String job;
    String email;

    public Member toEntity() {
        return new Member(0, this.name, this.age, JobType.toJobType(this.getJob()), this.email);
    }
}

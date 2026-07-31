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
public class MemberUpdateRequestDto {
    Integer id;
    String name;
    Integer age;
    JobType job;
    String email;

    public Member toEntity() {
        return new Member(this.id, this.name, this.age, this.getJob(), this.email);
    }
}

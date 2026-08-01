package com.demo.dto;


import com.demo.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberUpsertRequestDto {
    String name;
    Integer age;
    String job;
    String email;


    public Member toEntity() {
        return new Member(0, this.name, this.age, this.job, this.email);
    }
}

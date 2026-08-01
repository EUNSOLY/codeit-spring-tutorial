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
public class MemberUpsertRequestDto {

    String name;
    Integer age;
    String job;
    String email;

    // create 변환용
    public Member toEntity() {
        return new Member(null, this.name, this.age, JobType.toJobType(this.getJob()), this.email);
    }

    // update 변환용
    public Member toEntity(Integer id) {
        return new Member(id, this.name, this.age, JobType.toJobType(this.getJob()), this.email);
    }

    @Override
    public String toString() {
        return String.format("MemberUpsertRequestDto(" +
                "name=%s, age=%s, job=%s, email=%s" +
                ")");
    }
}

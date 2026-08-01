package com.demo.dto;

import com.demo.entity.Member;
import com.demo.enums.JobType;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberPatchRequestDto {
    @Null
    String name;
    @Null
    Integer age;
    @Null
    String job;
    @Null
    String email;

    // update 변환용
    public Member toEntity(Integer id) {
        return new Member(id, this.name, this.age, JobType.toJobType(this.getJob()), this.email);
    }

    @Override
    public String toString() {
        return String.format("MemberUpsertRequestDto(" +
                "name=%s, age=%s, job=%s, email=%s" +
                ")", this.name, this.age, this.job, this.email);
    }
}

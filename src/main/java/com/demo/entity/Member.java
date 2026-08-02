package com.demo.entity;


import com.demo.dto.MemberUpsertRequestDto;
import com.demo.enums.JobType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member extends Human {
    Integer age;
    JobType job;
    String email;

    private Member(Integer id, String name, Integer age, String job, String email) {
        super(id, name, false);
        this.age = age;
        this.job = job == null ? JobType.DEVELOPER : JobType.of(job);
        this.email = email;
    }

    public void updateMember(String name, Integer age, String job, String email) {
        if (name != null) super.updateName(name);
        if (age != null) this.age = age;
        if (job != null) this.job = JobType.of(job);
        if (email != null) this.email = email;
    }

    // 최초 생성 Member Entity 생성 시 id 값 null 처리를 위해 정적 메서드 사용
    public static Member toEntity(MemberUpsertRequestDto request) {
        return new Member(null, request.getName(), request.getAge(), request.getJob(), request.getEmail());
    }
}

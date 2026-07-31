package com.demo.entity;


import com.demo.enums.JobType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Member extends Human {
    Integer age;
    JobType job;
    String email;

    public Member(Integer id, String name, Integer age, String job, String email) {
        super(id, name);
        this.age = age;
        this.job = JobType.toJobType(job);
        this.email = email;
    }
}

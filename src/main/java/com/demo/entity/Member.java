package com.demo.entity;


import com.demo.enums.JobType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member extends Human {
    Integer age;
    JobType job = JobType.DEVELOPER;
    String email;

    public Member(Integer id, String name, Integer age, JobType job, String email) {
        super(id, name, false);
        this.age = age;
        this.job = job;
        this.email = email;
    }

    public void update(String name, Integer age, String job, String email) {
        if (name != null) super.changeName(name);
        if (age != null) this.age = age;
        if (job != null) this.job = JobType.toJobType(job);
        if (email != null) this.email = email;
    }
}

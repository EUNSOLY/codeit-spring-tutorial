package com.demo.entity;


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


    public Member(Integer id, String name, Integer age, String job, String email) {
        super(id, name);
        this.age = age;
        this.job = JobType.of(job);
        this.email = email;
    }

    public void updateMember(String name, Integer age, String job, String email) {
        if (name != null) super.updateName(name);
        if (age != null) this.age = age;
        if (job != null) this.job = JobType.of(job);
        if (email != null) this.email = email;
    }

}

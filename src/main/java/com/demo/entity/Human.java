package com.demo.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Human {
    Integer id;
    String name;
    boolean isDeleted;

    public void assignId(Integer id) {
        if (Objects.nonNull(this.id)) {
            throw new RuntimeException("이미 ID가 지정된 회원입니다.");
        }
        this.id = id;

    }

    protected void updateName(String name) {
        this.name = name;
    }

    protected boolean humanDelete() {
        return this.isDeleted = true;
    }

}

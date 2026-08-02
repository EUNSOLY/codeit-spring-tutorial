package com.demo.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Human {
    final Integer id;
    String name;
    boolean isDeleted;

    public void updateName(String name) {
        this.name = name;
    }

    public boolean humanDelete() {
        return this.isDeleted = true;
    }

}

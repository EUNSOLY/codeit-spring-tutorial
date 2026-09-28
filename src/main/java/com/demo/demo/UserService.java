package com.demo.demo;

public class UserService {

    public String create(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름이 존재해야합니다.");
        }
        return name;
    }

}

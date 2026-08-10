package com.demo.domain.user;

import lombok.Getter;

@Getter
public class User {
    private static int USER_CURRENT_ID = 0;

    private static int idGenerate() {
        return ++USER_CURRENT_ID;
    }

    private Integer id;
    private String name; // 사용자명
    private boolean deleted = false; // 사용자 삭제 여부

    private User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User create(String name) {
        int generatedId = idGenerate();
        return new User(generatedId, name);
    }
}

package com.demo.domain.user;

import com.demo.domain.common.BaseEntity;
import lombok.Getter;

@Getter
public class User extends BaseEntity {
    private static int USER_CURRENT_ID = 0;

    private static int idGenerate() {
        return ++USER_CURRENT_ID;
    }

    private String name; // 사용자명

    private User(Integer id, Integer createdByUserId, String name) {
        super(id, createdByUserId);
        this.name = name;
    }

    public static User create(
            String name,
            Integer createdByUserId /* 누가 유저를 생성했는지 */
    ) {
        int generatedId = idGenerate();
        return new User(generatedId, createdByUserId, name);
    }
}

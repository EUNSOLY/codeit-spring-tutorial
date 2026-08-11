package com.demo.domain.user;

import com.demo.domain.common.BaseEntity;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class User extends BaseEntity {
    private static int USER_CURRENT_ID = 0;

    private static int idGenerate() {
        return ++USER_CURRENT_ID;
    }

    private String name; // 사용자명
    private UserGrade grade = UserGrade.BRONZE;
    private int point = 0;

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

    // 포인트
    public void earn(int paidPrice) {
        this.point += (int) (paidPrice * this.grade.getEarningRate());
    }

    
    public void update(String name, UserGrade grade, int point) {
        this.name = name;
        this.grade = grade;
        this.point = point;
        super.updated();
    }

}

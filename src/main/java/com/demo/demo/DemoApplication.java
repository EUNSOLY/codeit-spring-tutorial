package com.demo.demo;

import java.sql.SQLException;

//@SpringBootApplication
public class DemoApplication {

    public static void connect(String username, String password) {
        try {
            if (username.equals("admin") && password.equals("1234")) {
                System.out.println("데이터 베이스 접속 성공");
            } else {
                throw new SQLException("데이터 베이스 접속 실패 - 사유 : ID / PW 일치하지 않음");
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println("========== 프로그램이 시작되었습니다. ==========");
        connect("aaron", "7890");
        System.out.println("========== 프로그램이 중간에 멈추지 않고, 정상적으로 종료되었습니다. = exit code 0 ==========");
    }

}

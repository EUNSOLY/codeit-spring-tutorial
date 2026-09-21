package com.demo.demo;

import com.demo.demo.exception.DatabaseException;
import com.demo.demo.exception.PasswordException;
import com.demo.demo.exception.UsernameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

//@SpringBootApplication
@Slf4j
public class DemoApplication {

    public static void connect(String username, String password) {
        if (!StringUtils.hasLength(username)) {
            throw new UsernameException();
        }

        if (!StringUtils.hasLength(password)) {
            throw new PasswordException();
        }

        if (!username.equals("admin") || !password.equals("1234")) {
            throw new DatabaseException("ID / PW 일치하지 않음");
        }
    }

    public static void main(String[] args) {
        System.out.println("========== 프로그램이 시작되었습니다. ==========");
        try {
//          connect(null, "7890");
//          connect("aaron", null);
            connect("aaron", "7890");
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
        System.out.println("========== 프로그램이 중간에 멈추지 않고, 정상적으로 종료되었습니다. = exit code 0 ==========");
    }

}

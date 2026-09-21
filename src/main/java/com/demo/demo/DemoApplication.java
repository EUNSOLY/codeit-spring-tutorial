package com.demo.demo;

import com.demo.demo.exception.CustomException;
import com.demo.demo.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

//@SpringBootApplication
@Slf4j
public class DemoApplication {

    public static void connect(String username, String password) {
        if (true) {
            throw new RuntimeException("예상치 못한 갑작스런 예외/오류 발생");
        }
        if (!StringUtils.hasLength(username)) {
            throw new CustomException(ErrorType.USERNAME_NOT_EXIST);
        }

        if (!StringUtils.hasLength(password)) {
            throw new CustomException(ErrorType.PASSWORD_NOT_EXIST);
        }

        if (!username.equals("admin") || !password.equals("1234")) {
            throw new CustomException(ErrorType.AUTHENTICATION_FAILED);
        }

        System.out.println("- 데이터 베이스 접속 성공");
    }

    public static void main(String[] args) {
        System.out.println("========== 프로그램이 시작되었습니다. ==========");
        try {
//          connect(null, "7890");
//            connect("aaron", null);
            connect("aaron", "7890");
//            connect("admin", "1234");
        } catch (CustomException e) {
            log.makeLoggingEventBuilder(e.getType().getLevel())
                    .log(e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
        System.out.println("========== 프로그램이 중간에 멈추지 않고, 정상적으로 종료되었습니다. = exit code 0 ==========");
    }

}

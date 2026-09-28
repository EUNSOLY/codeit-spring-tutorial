package com.demo.demo;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


/**
 * - `MethodOrderer.MethodName` : 이름순 정렬
 * - `MethodOrderer.DisplayName` : 표시 이름순 정렬
 * - `MethodOrderer.Random` : 랜덤 순서
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    @Test
    @Order(2)
    void secondTest() {
        System.out.println("두번째로 테스트코드 실행");
    }


    @Test
    @Order(1)
    void firstTest() {
        System.out.println("첫번째로 테스트코드 실행");
    }

    /**
     * 실행결과
     * 첫번째로 테스트코드 실행
     * 두번째로 테스트코드 실행
     */

}

package com.demo.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 기본값(Lifecycle.PER_METHOD) : 테스트 메서드 마다 새로운 테스트 클래스 인스턴스를 생성
// TestInstance.Lifecycle.PER_CLASS 지정 시 테스트 클래스 전체 스코프에서 오직 한개의 인스턴스만 생성
public class LifecycleTest {
    private int counter = 0;

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll 실행");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforEach 실행");
    }

    @Test
    void test1() {
        counter++;
        System.out.println("test1 counter = " + counter);
        assertEquals(1, counter);
    }

    @Test
    void test2() {
        counter++;
        System.out.println("test2 counter = " + counter);
        assertEquals(2, counter);
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach 실행");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll 실행");
    }


    /**
     * 테스트 실행 값
     * BeforeAll 실행
     * BeforEach 실행
     * test1 counter = 1
     * AfterEach 실행
     * BeforEach 실행
     * test2 counter = 1
     * AfterEach 실행
     * 즉 상태 공유 따위 되지않음!
     */

}

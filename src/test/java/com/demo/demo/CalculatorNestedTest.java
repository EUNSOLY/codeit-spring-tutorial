package com.demo.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorNestedTest {
    Calculator calculator = new Calculator();

    @Nested // 클래스를 활용한 중첩 테스트 구조(그룹화)
    @DisplayName("덧셈 테스트")
    class AddTests {

        @Test
        void addPositiveNumbers() {
            assertEquals(5, calculator.add(2, 3));
            System.out.println("AddTests - addPositiveNumbers 실행");
        }

        @Test
        void addNegativeNumbers() {
            assertEquals(-5, calculator.add(-2, -3));
            System.out.println("AddTests - addNegativeNumbers 실행");
        }
    }

    @Nested // 클래스를 활용한 중첩 테스트 구조(그룹화)
    @DisplayName("나눗셈 테스트")
    class DivideTests {
        @Test
        void divideSuccess() {
            assertEquals(2, calculator.divide(4, 2));
            System.out.println("DivideTests - divideSuccess 실행");
        }

        @Test
        void divideByZero() {
            assertThrows(IllegalArgumentException.class,
                    () -> calculator.divide(4, 0));
            System.out.println("DivideTests - divideByZero 실행");
        }
    }

    /**
     * CalculatorNestedTest 실행 결과 (각각 클래스들 개별로도 실행가능)
     * DivideTests - divideSuccess 실행
     * DivideTests - divideByZero 실행
     * AddTests - addPositiveNumbers 실행
     * AddTests - addNegativeNumbers 실행
     */
}



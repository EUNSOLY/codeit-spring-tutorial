package com.demo.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private Calculator basicCalculator;

    @Test
    void test() {
        long factorialAnswer = basicCalculator.factorial(10000);
        System.out.printf("factorial 결과 : %d \n ", factorialAnswer);
        long factPlusAnswer = basicCalculator.factPlus(10000, 100);
        System.out.printf("factPlus 결과 : %d \n ", factPlusAnswer);
        long minusAnswer = basicCalculator.minus(10000, 100);
        System.out.printf("minus 결과 : %d \n ", minusAnswer);
    }

}

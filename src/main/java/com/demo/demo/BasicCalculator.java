package com.demo.demo;

import org.springframework.stereotype.Component;

@Component
public class BasicCalculator implements Calculator {
    @Override
    public long factorial(long num) {
        // 방금 구현한 factorial의 실행시간을 구하고싶음
        long result = 1;
        for (long i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    @Override
    public long factPlus(long num1, long num2) {
        return num1 + num2;
    }

    @Override
    public long minus(long num1, long num2) {
        return num1 - num2;
    }
}

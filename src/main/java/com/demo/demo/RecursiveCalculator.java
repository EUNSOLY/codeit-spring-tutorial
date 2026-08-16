package com.demo.demo;

public class RecursiveCalculator implements Calculator {
    @Override
    public long factorial(long num) {
        if (num == 0) {
            return 1;
        }
        return num * factorial(num - 1);
    }

    @Override
    public long factPlus(long num1, long num2) {
        return 0;
    }

    @Override
    public long minus(long num1, long num2) {
        return 0;
    }
}

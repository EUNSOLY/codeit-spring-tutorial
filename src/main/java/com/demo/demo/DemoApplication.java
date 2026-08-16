package com.demo.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);

        Calculator basic = new BasicCalculator();
        Calculator recursive = new RecursiveCalculator();

        long basicStart = System.nanoTime();
        basic.factorial(10000);
        long basicEnd = System.nanoTime();
        System.out.printf("Basic Calculator의 factorial(100) 실행 시간 -> %d \n", (basicEnd - basicStart));

        long recursiveStart = System.nanoTime();
        recursive.factorial(10000);
        long recursiveEnd = System.nanoTime();
        System.out.printf("Recursive Calculator의 factorial(100) 실행 시간 -> %d \n", (recursiveEnd - recursiveStart));
    }

}

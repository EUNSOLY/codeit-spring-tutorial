package com.demo.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);

        Calculator basic = new BasicCalculator();
        Calculator recursive = new RecursiveCalculator();

        long factorialAnswer = basic.factorial(10000);
        System.out.printf("factorial 결과 : %d \n ", factorialAnswer);
        long factPlusAnswer = basic.factPlus(10000, 100);
        System.out.printf("factPlus 결과 : %d \n ", factorialAnswer);
        long minusAnswer = basic.minus(10000, 100);
        System.out.printf("minus 결과 : %d \n ", minusAnswer);

        recursive.factorial(10000);
        recursive.factPlus(10000, 100);
        recursive.minus(10000, 100);
    }

}

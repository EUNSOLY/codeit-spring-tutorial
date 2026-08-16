package com.demo.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);

        Calculator proxyCalculator1 = new ExecutionTimeCalculator(new BasicCalculator());
        proxyCalculator1.factorial(1000);

        Calculator proxyCalculator2 = new ExecutionTimeCalculator(new RecursiveCalculator());
        proxyCalculator2.factorial(1000);

    }

}

package com.demo.demo;

// 실행시간을 측정하는 객체로 프록시 역활 - Calculator를 구현한 구현체
public class ExecutionTimeCalculator implements Calculator {
    private final Calculator delegate; // 내부에 Calculator를 필드로 갖고

    public ExecutionTimeCalculator(
            final Calculator delegate // 외부에서 구현체의 의존성을 주입해준다.
    ) {
        this.delegate = delegate;
    }

    @Override
    public long factorial(long num) {
        // 핵심 연산에서는 부가기능인 실행시간을 측정해주고
        long start = System.nanoTime();
        long result = delegate.factorial(num); // 핵심 연산인 실행시간측정은 외부에서 주입받은 delegate에게 위임한다.
        long end = System.nanoTime();

        System.out.printf("%s의 factorial(%d) 실행시간 -> %d \n",
                delegate.getClass().getSimpleName(),
                num,
                (end - start)
        );
        return result;
    }
}

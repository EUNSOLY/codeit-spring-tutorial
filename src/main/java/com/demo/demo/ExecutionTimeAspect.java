package com.demo.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ExecutionTimeAspect {
    // AspectJ 문법 : Pointcut 표현식 = 지시자(패턴)
    @Pointcut("execution(* fact*(..))")
    private void publicTarget() {
    }

    @Around("publicTarget()") //@Pointcut은 조건식에 이름을 붙여 재사용 가능하게 하는 것. @Around("메서드명()")처럼 그 이름을 참조하면 해당 조건이 그대로 적용됨.
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.nanoTime();
            Signature sig = joinPoint.getSignature();
            System.out.printf("%s.%s(%s) 실행시간 : %d \n",
                    joinPoint.getTarget().getClass().getSimpleName(), // 클래스명
                    sig.getName(), // 메서드명
                    Arrays.toString(joinPoint.getArgs()), // 메서드 파라미터
                    (finish - start) // 시간
            );

        }
    }
}

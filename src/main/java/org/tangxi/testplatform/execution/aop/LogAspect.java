package org.tangxi.testplatform.execution.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class LogAspect {
    private List<String> logs = new ArrayList<>();

    @Before("@annotation(LOG)")
    public void doBefore(JoinPoint joinPoint){
        logs.add("开始调用方法");
    }

    public List<String> getLogs(){
        return this.logs;
    }

//    @Around("@annotation(LOG)")
//    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("#################################################################");
//        Object proceed = joinPoint.proceed();
//        return proceed;
//    }
}

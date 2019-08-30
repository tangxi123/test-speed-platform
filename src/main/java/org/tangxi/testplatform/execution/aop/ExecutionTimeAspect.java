package org.tangxi.testplatform.execution.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseAssertionError;
import org.tangxi.testplatform.common.exception.testcase.TestCaseNotFoundException;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.TestCase;

import java.time.LocalDateTime;


@Aspect
@Component
public class ExecutionTimeAspect {

    @Autowired
    TestCaseMapper testCaseMapper;

    @Autowired
    TestCaseWrapper testCaseWrapper;

    @Around("@annotation(ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        int id = (int)args[0];
        TestCase testCase = testCaseMapper.getTestCaseById(id);
        if(testCase == null){
            throw new TestCaseNotFoundException("未找到测试用例");
        }
        LocalDateTime start_time = null;
        LocalDateTime end_time = null;
        long executionTime = 0;
        try {
            long start = System.currentTimeMillis();
            start_time = LocalDateTime.now();
            Object proceed = joinPoint.proceed();
            executionTime = System.currentTimeMillis() - start;
            return proceed;
        }catch (TestCaseNotFoundException e){
            throw new TestCaseNotFoundException(e);
        } catch (TestCaseRunException e){
            throw new TestCaseRunException(e);
        } catch (UnexpectedTestCaseException e) {
            throw new UnexpectedTestCaseException(e);
        } catch (TestCaseAssertionError e){
            throw new TestCaseAssertionError(e);
        } catch (Throwable e) {
            throw new UnexpectedTestCaseException(e);
        } finally {
            end_time = LocalDateTime.now();
            setRunTime(testCase,start_time,end_time,executionTime);
        }
    }

    private void setRunTime(TestCase testCase,LocalDateTime startTime,LocalDateTime endTime,long executionTime){
            testCase.setStartTime(startTime);
            testCase.setEndTime(endTime);
            testCase.setExecutionTime(executionTime);
            testCaseMapper.updateTestcase(testCase);


    }

}

package org.tangxi.testplatform.common.exception.testcase;

public class TestCaseNotFoundException extends RuntimeException{
    public TestCaseNotFoundException(Throwable e){
        super(e);
    }

    public TestCaseNotFoundException(String message, Throwable e){
        super(message,e);
    }

    public TestCaseNotFoundException(String message){
        super(message);
    }
}

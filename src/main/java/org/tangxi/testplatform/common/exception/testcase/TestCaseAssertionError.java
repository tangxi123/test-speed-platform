package org.tangxi.testplatform.common.exception.testcase;

public class TestCaseAssertionError extends RuntimeException {

    public TestCaseAssertionError(Throwable e){
        super(e);
    }

    public TestCaseAssertionError(String message, Throwable e){
        super(message,e);
    }

    public TestCaseAssertionError(String message){
        super(message);
    }
}

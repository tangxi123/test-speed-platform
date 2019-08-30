package org.tangxi.testplatform.common.exception.testcase;

public class TestCaseRunException extends RuntimeException {
    public TestCaseRunException(Throwable e){
        super(e);
    }

    public TestCaseRunException(String message, Throwable e){
        super(message,e);
    }

    public TestCaseRunException(String message){
        super(message);
    }
}

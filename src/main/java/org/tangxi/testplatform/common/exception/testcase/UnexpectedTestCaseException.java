package org.tangxi.testplatform.common.exception.testcase;

public class UnexpectedTestCaseException extends RuntimeException {
    public UnexpectedTestCaseException(Throwable e){
        super(e);
    }

    public UnexpectedTestCaseException(String message, Throwable e){
        super(message,e);
    }

    public UnexpectedTestCaseException(String message){
        super(message);
    }
}

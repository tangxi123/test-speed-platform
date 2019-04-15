package org.tangxi.testplatform.common.exception;

public class UnexpectedTestCaseException extends RuntimeException {
    public UnexpectedTestCaseException(Throwable e){
        super(e);
    }
}

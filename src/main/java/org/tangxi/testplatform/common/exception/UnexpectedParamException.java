package org.tangxi.testplatform.common.exception;

public class UnexpectedParamException extends RuntimeException {
    public UnexpectedParamException(Throwable e){
        super(e);
    }
}

package org.tangxi.testplatform.common.exception;

public class UnexpectedActionException extends RuntimeException {
    public UnexpectedActionException(Throwable e){
        super(e);
    }
}

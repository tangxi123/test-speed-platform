package org.tangxi.testplatform.common.exception;

public class UnexpectedDatabaseConfigException extends RuntimeException {
    public UnexpectedDatabaseConfigException(Throwable e){
        super(e);
    }
}

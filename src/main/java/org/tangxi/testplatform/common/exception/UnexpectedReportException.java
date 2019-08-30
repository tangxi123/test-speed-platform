package org.tangxi.testplatform.common.exception;

public class UnexpectedReportException extends RuntimeException{
    public UnexpectedReportException(Throwable e){
        super(e);
    }
}

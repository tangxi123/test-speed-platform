package org.tangxi.testplatform.common.exception;

public class ParamDuplicateException extends RuntimeException {
    private String name;

    public ParamDuplicateException(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

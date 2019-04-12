package org.tangxi.testplatform.common.exception;

import org.springframework.dao.DuplicateKeyException;

public class TestCaseDuplicateException extends RuntimeException {
    private String testName;

    public TestCaseDuplicateException(String testName){
        this.testName = testName;
    }

    public String getTestName(){
        return testName;
    }
}

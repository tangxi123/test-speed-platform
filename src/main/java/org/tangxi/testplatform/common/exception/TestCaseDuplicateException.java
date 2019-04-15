package org.tangxi.testplatform.common.exception;


public class TestCaseDuplicateException extends RuntimeException {
    private String testName;

    public TestCaseDuplicateException(String testName){
        this.testName = testName;
    }

    public String getTestName(){
        return testName;
    }
}

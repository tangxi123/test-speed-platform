package org.tangxi.testplatform.common.exception;

public class ActionDuplicateException extends RuntimeException {
    private String actionName;

    public ActionDuplicateException(String actionName){
        this.actionName = actionName;
    }

    public String getActionName(){
        return actionName;
    }
}

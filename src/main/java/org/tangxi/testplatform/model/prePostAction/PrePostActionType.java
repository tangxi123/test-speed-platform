package org.tangxi.testplatform.model.prePostAction;

/**
 * 前后置动作的枚举类
 * @author Tangx
 * 2019-02-13 15:02
 */
public enum PrePostActionType {
    SQL("sql");

    private String field;

    PrePostActionType(String field){
        this.field= field;
    }
//    private int code;
//    private String value;
//    PrePostActionType(int code,String value){
//        this.code = code;
//        this.value = value;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public int getCode() {
//        return code;
//    }
}

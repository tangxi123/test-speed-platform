package org.tangxi.testplatform.model.parameter;

/**
 * ParameterType枚举类
 * @author Tangx
 * 2019-02-13 14:00
 */
public enum ParameterType {
    SQL("sql"),
    TOKEN("token"),
    KEYVALUE("keyValue");


    private String field;

    ParameterType(String field){
        this.field= field;
    }
}

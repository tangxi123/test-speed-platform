package org.tangxi.testplatform.model.prePostAction;

/**
 * 前后置动作的枚举类
 * @author Tangx
 * 2019-02-13 15:02
 */
public enum PrePostActionType {
    SQL("sql");
    private String field;
    PrePostActionType(String field){this.field = field;}
}

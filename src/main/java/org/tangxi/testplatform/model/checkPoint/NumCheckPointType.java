package org.tangxi.testplatform.model.checkPoint;

/**
 * 不同数值判断类型:
 *      等于：=
 *      大于：>
 *      小于：<
 *      大于等于：>=
 *      小于等于:<=
 *
 * @author Tangx
 * 2019-02-28 12:28
 */
public enum NumCheckPointType implements CheckPointType {
    NUM_EQ("等于"),
    NUM_GT("大于"),
    NUM_LT("小于"),
    NUM_LT_EQ("小于等于"),
    NUM_GT_EQ("大于等于");

    private String field;

    NumCheckPointType(String field){
        this.field= field;
    }
}

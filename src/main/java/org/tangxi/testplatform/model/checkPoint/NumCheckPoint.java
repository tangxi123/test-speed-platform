package org.tangxi.testplatform.model.checkPoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 数值checkPoint的pojo
 * @author TangXi
 * 2019-02-28 12:45
 */
@JsonTypeName("NumCheckPoint")
public class NumCheckPoint implements CheckPoint {
    //将要验证的类型
    private String type;

    //数值验证类型
    private NumCheckPointType numCheckPointType;

    //验证key
    private String checkKey;

    //期望值
    private String expected;

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NumCheckPointType getNumCheckPointType() {
        return numCheckPointType;
    }

    public void setNumCheckPointType(NumCheckPointType numCheckPointType) {
        this.numCheckPointType = numCheckPointType;
    }

    @Override
    public String getCheckKey() {
        return checkKey;
    }

    public void setCheckKey(String checkKey) {
        this.checkKey = checkKey;
    }

    @Override
    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    @JsonIgnore
    @Override
    public CheckPointType getCheckPointType() {
        return numCheckPointType;
    }
}


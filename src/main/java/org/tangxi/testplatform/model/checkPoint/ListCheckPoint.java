package org.tangxi.testplatform.model.checkPoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ListCheckPoint")
public class ListCheckPoint implements CheckPoint {
    private String key;
    //将要验证的类型
    private String type;

    //数值验证类型
    private ListCheckPointType listCheckPointType;

    //验证key
    private String checkKey;

    //期望值
    private String expected;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ListCheckPointType getListCheckPointType() {
        return listCheckPointType;
    }

    public void setListCheckPointType(ListCheckPointType listCheckPointType) {
        this.listCheckPointType = listCheckPointType;
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
        return listCheckPointType;
    }


}
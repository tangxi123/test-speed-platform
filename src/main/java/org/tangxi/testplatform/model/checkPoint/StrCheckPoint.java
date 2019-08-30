package org.tangxi.testplatform.model.checkPoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/***
 * 字符串类型检查类
 */
@JsonTypeName("StrCheckPoint")
public class StrCheckPoint implements CheckPoint {
    private String key;
    private String type;
    private StrCheckPointType strCheckPointType;
    private String checkKey;
    private String expected;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StrCheckPointType getStrCheckPointType() {
        return strCheckPointType;
    }

    public void setStrCheckPointType(StrCheckPointType strCheckPointType) {
        this.strCheckPointType = strCheckPointType;
    }

    @JsonIgnore
    public CheckPointType getCheckPointType() {
        return strCheckPointType;
    }


    public String getCheckKey() {
        return checkKey;
    }

    public void setCheckKey(String checkKey) {
        this.checkKey = checkKey;
    }


    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}

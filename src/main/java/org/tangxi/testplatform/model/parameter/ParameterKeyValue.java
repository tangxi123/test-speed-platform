package org.tangxi.testplatform.model.parameter;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ParameterKeyValue")
public class ParameterKeyValue implements Parameter {
    private String id;
    private String key;
    private String value;
    private int paramId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getParamId() {
        return paramId;
    }

    @Override
    public void setParamId(int paramId) {
        this.paramId = paramId;
    }
}

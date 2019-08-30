package org.tangxi.testplatform.model.parameter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Parameter接口
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "type",visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "ParameterSql",value = ParameterSql.class),
        @JsonSubTypes.Type(name = "ParameterToken",value = ParameterToken.class),
        @JsonSubTypes.Type(name = "ParameterKeyValue",value = ParameterKeyValue.class)
})
public interface Parameter {
    void setParamId(int id);
}

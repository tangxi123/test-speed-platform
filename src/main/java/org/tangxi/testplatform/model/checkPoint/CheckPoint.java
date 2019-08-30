package org.tangxi.testplatform.model.checkPoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "type",visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "StrCheckPoint",value = StrCheckPoint.class),
        @JsonSubTypes.Type(name = "NumCheckPoint",value = NumCheckPoint.class),
        @JsonSubTypes.Type(name = "ListCheckPoint",value = ListCheckPoint.class)

})
public interface CheckPoint {
    String getType();
    CheckPointType getCheckPointType();
    String getCheckKey();
    String getExpected();
    void setCheckKey(String checkKey);
    void setExpected(String expected);
}

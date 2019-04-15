package org.tangxi.testplatform.model.prePostAction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "type",visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "PrePostActionSql",value = PrePostActionSql.class)
})
public interface PrePostAction {
    void setActionId(int id);
}

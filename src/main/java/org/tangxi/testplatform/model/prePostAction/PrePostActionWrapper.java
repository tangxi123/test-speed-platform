package org.tangxi.testplatform.model.prePostAction;

import org.tangxi.testplatform.common.util.JacksonUtil;

/**
 * 前后置动作包装类
 */
public class PrePostActionWrapper {
    private int id;

    private String name;

    private String descs;

    private PrePostActionType actionType;

    private PrePostAction action;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public PrePostActionType getActionType() {
        return actionType;
    }

    public void setActionType(PrePostActionType actionType) {
        this.actionType = actionType;
    }

    public PrePostAction getAction() {
        return action;
    }

    public void setAction(PrePostAction action) {
        this.action = action;
    }

    @Override
    public String toString(){
        return JacksonUtil.toJson(this);
    }
}

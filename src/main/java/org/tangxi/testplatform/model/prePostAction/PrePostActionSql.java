package org.tangxi.testplatform.model.prePostAction;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

/**
 * SQL类型的前后置动作
 */
@JsonTypeName("PrePostActionSql")
public class PrePostActionSql implements PrePostAction {
    private int id;
    private List<SqlInfo> sql;
    private int actionId;
    private String dbConfigId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public List<SqlInfo> getSql() {
        return sql;
    }

    public void setSql(List<SqlInfo> sql) {
        this.sql = sql;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getDbConfigId() {
        return dbConfigId;
    }

    public void setDbConfigId(String dbConfigId) {
        this.dbConfigId = dbConfigId;
    }
}

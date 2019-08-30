package org.tangxi.testplatform.model.parameter;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Sql类型的Parameter
 */

@JsonTypeName("ParameterSql")
public class ParameterSql implements Parameter {
    private int id;
    private String sql;
    private String param;
    private int paramId;
    private int dbConfigId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public int getDbConfigId() {
        return dbConfigId;
    }

    public void setDbConfigId(int dbConfigId) {
        this.dbConfigId = dbConfigId;
    }
}

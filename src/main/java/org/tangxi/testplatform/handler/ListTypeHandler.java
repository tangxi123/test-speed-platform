package org.tangxi.testplatform.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.tangxi.testplatform.common.util.JacksonUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ListTypeHandler<T> extends BaseTypeHandler<List<T>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null){
            return;
        }
        String json = JacksonUtil.toJson(parameter);
        ps.setString(i,json);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return getColumnValueResult(columnValue);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return getColumnValueResult(columnValue);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return getColumnValueResult(columnValue);
    }

    private List<T> getColumnValueResult(String columnValue){
        if(columnValue != null){
            return JacksonUtil.fromJson(columnValue, new TypeReference<List<T>>() {
            });
        }
        return null;
    }
}

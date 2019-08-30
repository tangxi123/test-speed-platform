package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.*;

import java.util.LinkedHashMap;
import java.util.List;

public interface ExecuteSqlMapper {
    @Select("${value}")
    List<LinkedHashMap<String, Object>> select(String sql);
    @Insert("${value}")
    int insert(String sql);
    @Update("${value}")
    int update(String sql);
    @Delete("${value}")
    int delete(String sql);
}

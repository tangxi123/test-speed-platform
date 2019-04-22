package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.parameter.Parameter;
import org.tangxi.testplatform.model.parameter.ParameterSql;
import org.tangxi.testplatform.model.parameter.ParameterWrapper;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ParameterMapper {
    //向parameter主表插入数据
    int createParam(ParameterWrapper parameterWrapper);

    //向parameter从表插入数据
    int createSqlParam(Parameter parameter);

    //更新parameter主表数据
    int updateParam(ParameterWrapper parameterWrapper);

    //更新parameter从表数据
    int updateSqlParam(Parameter parameter);

    //根据id查询参数
    ParameterWrapper getParamById(@Param("id")int id);

    //根据参数名字或描述分页查询参数列表
    List<ParameterWrapper> getParamsByFields(Map<String, Object> params);

    //根据id删除参数
    int deleteParamById(int id);

    //根据name查询某个参数的个数
    int getParamCountByName(@Param("name") String name);
}

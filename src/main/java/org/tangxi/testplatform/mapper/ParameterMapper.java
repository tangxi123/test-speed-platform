package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.parameter.Parameter;
import org.tangxi.testplatform.model.parameter.ParameterWrapper;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ParameterMapper {
    //向parameter主表插入数据
    int createParam(ParameterWrapper parameterWrapper);

    //向parameterSql从表插入数据
    int createSqlParam(Parameter parameter);

    //向parameterToken从表插入数据
    int createTokenParam(Parameter parameter);

    //像parameterKeyValue从表插入数据
    int createKeyValueParam(Parameter parameter);

    //更新parameter主表数据
    int updateParam(ParameterWrapper parameterWrapper);

    //更新parameterSql从表数据
    int updateSqlParam(Parameter parameter);

    //更新parameterToken从表数据
    int updateTokenParam(Parameter parameter);

    //更新parameterKeyValue从表数据
    int updateKeyValueParam(Parameter parameter);

    //根据参数名称、描述分页查询前后置动作不包括明细
    List<ParameterWrapper> getParamWrappers(Map<String,Object> params);

//    //查询所有的parameter主表数据
//    List<ParameterWrapper> getAllParameters();

    //根据id查询parameterWrapper数据
    ParameterWrapper getParamWrapperById(@Param("id") int id);
    //根据id查询sqlParam
    ParameterWrapper getSqlParamById(@Param("id")int id);
    //根据id查询tokenParam
    ParameterWrapper getTokenParamById(@Param("id") int id);

    //根据id查询keyValueParam
    ParameterWrapper getKeyValueParamById(@Param("id") int id);
    //根据参数名字或描述分页查询参数列表
    List<ParameterWrapper> getParamsByFields(Map<String, Object> params);

    //根据id删除参数
    int deleteParamById(int id);

    //根据模块id删除参数
    int deleteParamByModuleId(@Param("moduleId") int moduleId);

    //根据name查询某个参数的个数
    int getParamCountByName(@Param("name") String name);

    //根据name查询对应的id
    Integer getParamIdByName(@Param("name") String name);
}

package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.prePostAction.PrePostAction;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PrePostActionMapper {
    //插入前后置动作action主表数据
    int createAction(PrePostActionWrapper actionWrapper);

    //插入类型为Sql的明细数据prepostactionsql从表数据
    int createSqlAction(PrePostAction action);

    //更新前后置动作action主表数据
    int updateAction(PrePostActionWrapper actionWrapper);

    //更新类型为Sql的明细数据prepostactionsql从表数据
    int updateSqlAction(PrePostAction action);

    //获取所有的前后置动作不包括明细
    List<PrePostActionWrapper> getActionWrappers(Map<String,Object> params);

    //获取所有的前后置动作包括明细
    List<PrePostActionWrapper> getAllActions();

    //根据id查询sql类型的前后置动作
    PrePostActionWrapper getSqlActionById(@Param("id") int id);

    //根据id获取前后置动作
    PrePostActionWrapper getActionById(@Param("id") int id);

    //根据动作名称、描述分页查询前后置动作
    List<PrePostActionWrapper> getActionsByFields(Map<String,Object> params);

    //根据id删除前后置动作
    int deleteActionById(@Param("id") int id);

    //根据模块id删除前后置动作
    int deleteActionByModuleId(@Param("moduleId") int moduleId);

    //根据name查询某个前后置动作的个数
    int getActionCountByName(@Param("name") String name);
}

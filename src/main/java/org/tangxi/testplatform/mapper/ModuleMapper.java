package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.Module;
import org.tangxi.testplatform.model.ModuleTree;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ModuleMapper {

    //创建module
    int createModule(Module module);

    //更新module
    int updateModule(Module module);

    //根据id查询module值
    Module getModuleById(@Param("id") int id);

    //根据parentId查询相邻的子模块
    List<Module> getModulesByParentId(@Param("id") int id);

    //根据id查询所有子模块
    List<Module> getSubModulesById(@Param("id") int id);

    //根据parentId和searchKey查询子模块
    List<Module> getModulesByParentIdAndSearchKey(@Param("id") int id, @Param("searchKey") String searchKey);

    //根据模块id获取目录树
    List<ModuleTree> getTreeByModuleId(@Param("id") int id);

    //根据模块id删除项目以及项目下的测试用例以及基础配置
    int deleteModuleByModuleId(@Param("id") int id);
}


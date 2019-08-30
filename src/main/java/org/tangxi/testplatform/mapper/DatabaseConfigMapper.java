package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;


import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DatabaseConfigMapper {
    //创建databaseConfig
    int createDatabaseConfig(DatabaseConfig dbConfig);

    //更新databaseConfig
    int updateDatabaseConfig(DatabaseConfig dbConfig);

    //根据id查询databaseConfig
    DatabaseConfig getDatabaseConfigById(@Param("id") String id);

    //根据configName、备注分页查询所有的databaseConfig
    List<DatabaseConfig> getDatabaseConfigsByParams(Map<String,Object> params);

    //根据moduleId查询查询该模块下所有的databaseConfig
    List<DatabaseConfig> getAllDBConfigsByModuleId(@Param("moduleId") String moduleId);

    //根据id删除databaseCongfig
    int deleteDatabaseConfigById(@Param("id") String id);

    //根据模块id删除databaseCongfig
    int deleteDatabaseConfigByModuleId(@Param("moduleId") int moduleId);




}

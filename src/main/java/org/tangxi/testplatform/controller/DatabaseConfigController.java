package org.tangxi.testplatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;
import org.tangxi.testplatform.service.DatabaseConfigService;

import java.util.Map;

@RestController
@RequestMapping("/api/dbConfig")
public class DatabaseConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfigController.class);

    @Autowired
    DatabaseConfigService databaseConfigService;

    /**
     * 创建databaseConfig配置
     * @param databaseConfig
     * @return
     */
    @CrossOrigin
    @PostMapping("/create")
    public Response<?> createDatabaseConfig(@RequestBody DatabaseConfig databaseConfig){
        LOG.info("传入的参数为：", JacksonUtil.toJson(databaseConfig));
        return databaseConfigService.createDatabaseConfig(databaseConfig);
    }

    /**
     * 更新databaseConfig配置
     * @param databaseConfig
     * @return
     */
    @CrossOrigin
    @PutMapping("/update")
    public Response<?> updateDatabaseConfig(@RequestBody DatabaseConfig databaseConfig){
        LOG.info("传入的参数为：", JacksonUtil.toJson(databaseConfig));
        return databaseConfigService.updateDatabaseConfig(databaseConfig);
    }

    /**
     * 更新databaseConfig配置
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public Response<?> deleteDatabaseConfig(@PathVariable int id){
        LOG.info("传入的参数为：", JacksonUtil.toJson(id));
        return databaseConfigService.deleteDatabaseConfig(id);
    }

    /**
     * 根据id查询databaseConfig
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/{id}")
    public Response<?> getDatabaseConfigById(@PathVariable String id){
        LOG.info("传入的参数为：{}",id);
        return databaseConfigService.getDatabaseConfigById(id);
    }

    /**
     * 根据configName、描述分页查询databaseConfig
     * @param params
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/")
    public Response<?> getDatabaseConfigsByParams(@RequestParam Map<String,Object> params){
        LOG.info("传入的参数为：", JacksonUtil.toJson(params));
        return databaseConfigService.getDatabaseConfigsByParams(params);
    }

    /**
     * 根据模块id获取该模块下所有的数据库配置
     * @param moduleId
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/all/moduleId/{moduleId}")
    public Response<?> getAllDBConfigsByModuleId(@PathVariable String moduleId){
        return databaseConfigService.getAllDBConfigsByModuleId(moduleId);
    }

    /**
     * 检查配置的databaseConfig是否能进行连接
     */
    @CrossOrigin
    @GetMapping("/checkDBConnection")
    public Response<?> checkDBConnection(@RequestBody DatabaseConfig databaseConfig){
        LOG.info("传入的参数为：{}",JacksonUtil.toJson(databaseConfig));
        return databaseConfigService.checkDBConnection(databaseConfig);
    }
}

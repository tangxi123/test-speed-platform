package org.tangxi.testplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.UnexpectedModuleException;
import org.tangxi.testplatform.common.exception.UnexpectedUrlException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.mapper.*;
import org.tangxi.testplatform.model.Module;
import org.tangxi.testplatform.model.ModuleTree;

import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService {
    private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);

    @Autowired
    ModuleMapper moduleMapper;

    @Autowired
    TestCaseMapper testCaseMapper;

    @Autowired
    UrlMapper urlMapper;

    @Autowired
    DatabaseConfigMapper databaseConfigMapper;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    PrePostActionMapper prePostActionMapper;


    /**
     * 创建模块
     *
     * @param module
     * @return
     */
    public Response<String> createModule(@RequestBody Module module) {
        try {
            module.setCreateAt(LocalDateTime.now());
            module.setUpdateAt(module.getCreateAt());
            moduleMapper.createModule(module);
            return new Response<>(200,null,"创建成功");
        } catch (Exception e) {
            throw new UnexpectedModuleException(e);
        }

    }

    /**
     * 更新模块
     * @param module
     * @return
     */
    public Response<?> updateModule(Module module){
        try{
            module.setUpdateAt(LocalDateTime.now());
            moduleMapper.updateModule(module);
            return new Response<>(200,null,"更新成功");
        }catch (Exception e){
            throw new UnexpectedModuleException(e);
        }
    }

    /**
     * 根据id查询模块
     * @param id
     * @return
     */
    public Response<?> getModuleById(int id){
        try{
            Module module = moduleMapper.getModuleById(id);
            return new Response<>(200,module,"查询成功");
        }catch (Exception e){
            throw new UnexpectedModuleException(e);
        }
    }

    /**
     * 根据parentId查询其相邻的子模块
     * @param id
     * @return
     */
    public Response<?> getModulesByParentId(int id){
        try{
            List<Module> modules = moduleMapper.getModulesByParentId(id);
            return new Response<>(200,modules,"查询成功");
        }catch (Exception e){
            throw new UnexpectedModuleException(e);
        }
    }

    /**
     * 根据id查询所有子模块id
     */
    public Response<?> getSubModulesById(int id){
        try{
            List<Module> subModules = moduleMapper.getSubModulesById(id);
            return new Response<>(200,subModules,"查询成功");
        }catch (Exception e){
            throw new UnexpectedModuleException(e);
        }
    }

    /**
     * 根据id查询所有格式化后的模块，比如
     * [
     *  {
     *   "id":1,
     *   "name":"/testPlatForm-api"
     *  },
     *  {
     *      "id":2,
     *      "name":"/Z计划"
     *  },
     *  {
     *      "id":3,
     *      "name":"/testPlatForm-api/add-parameter"
     *  }
     * ]
     */
    public Response<?> getFormattedModules(int id){
        try{
            List<Module> subModules = moduleMapper.getSubModulesById(id);
            for(Module module : subModules){
                String moduleName = getChangedModuleName(module);
                LOG.info("Module为{}：", moduleName);
                module.setName(moduleName);
                }
            return new Response<>(200,subModules,"查询成功");
        }catch (Exception e) {
            throw new UnexpectedModuleException(e);
        }
    }


    /**
     * 根据parentId和searchKey查询子模块
     * @param id
     * @param searchKey
     * @return
     */
    public Response<?> getModulesByParentIdAndSearchKey(int id, String searchKey){
        try{
            List<Module> modules = moduleMapper.getModulesByParentIdAndSearchKey(id,searchKey);
            return new Response<>(200,modules,"查询成功");
        }catch (Exception e){
            throw new UnexpectedModuleException(e);
        }
    }

    /**
     * 根据模块id获取目录树
     * @param id
     * @return
     */
    public Response<?> getTreeByModuleId(int id){
        try{
            List<ModuleTree> tree = moduleMapper.getTreeByModuleId(id);
            return new Response<>(200,tree,"查询成功");
        }catch (Exception e){
            throw new UnexpectedModuleException(e);
        }
    }

    /**
     *用于getFormattedModules（）方法
     * 递归方法，当module里有父模块时，将module的name更改为加了父模块的name
     * @param module
     * @return
     */
    private String getChangedModuleName(Module module){
        if(!hasParent(module)){
            module.setName("/"+module.getName());
            return module.getName();
        }else{
            int parentId = module.getParentId();
            Module parentModule = moduleMapper.getModuleById(parentId);
            parentModule.setName(parentModule.getName()+"/"+module.getName());
            return getChangedModuleName(parentModule);
        }
    }

    /**
     * 用于getChangedModuleName（）方法
     * 判断module里是否有父模块
     * 如果parentId = 0则表示没有父模块，反之则有
     * @param module
     * @return
     */
    private boolean hasParent(Module module){
        if(module.getParentId() == 0){
            return false;
        }
        return true;
    }

    /**
     * 根据模块id删除模块及其下面的测试用例以及基础配置
     * @param id
     * @return
     */
    @Transactional
    public Response<?> deleteModuleByModuleId(int id){

        List<Module> subModules = moduleMapper.getSubModulesById(id);
        for(Module module : subModules){
            int moduleId = module.getId();
            int delUrlNum = urlMapper.deleteUrlByModuleId(moduleId);
            int delParamNum = parameterMapper.deleteParamByModuleId(moduleId);
            int delActionNum = prePostActionMapper.deleteActionByModuleId(moduleId);
            int delDatabaseConfigNum = databaseConfigMapper.deleteDatabaseConfigByModuleId(moduleId);
            int delTestCaseNum = testCaseMapper.delteTestCaseByModuleId(moduleId);
            int deleteModuleNum = moduleMapper.deleteModuleByModuleId(moduleId);
        }
        return new Response<>(200,null,"删除成功");
    }


}

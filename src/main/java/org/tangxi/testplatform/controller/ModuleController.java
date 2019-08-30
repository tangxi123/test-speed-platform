package org.tangxi.testplatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.Module;
import org.tangxi.testplatform.service.ModuleService;
import org.tangxi.testplatform.service.TestCaseService;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    private static final Logger LOG = LoggerFactory.getLogger(ModuleController.class);

    @Autowired
    ModuleService moduleService;

    /**
     * 创建模块
     * @param module
     * @return
     */
    @CrossOrigin
    @PostMapping("/create")
    public Response<?> createModule(@RequestBody Module module){
        LOG.info("传入的参数为：{}",JacksonUtil.toJson(module));
        return moduleService.createModule(module);
    }

    /**
     * 更新模块
     * @param module
     * @return
     */
    @CrossOrigin
    @PutMapping("/update")
    public Response<?> updateModule(@RequestBody Module module){
        LOG.info("传入的参数为：{}", JacksonUtil.toJson(module));
        return moduleService.updateModule(module);
    }

    /**
     * 根据id查询module
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/{id}")
    public Response<?> getModuleById(@PathVariable int id){
        LOG.info("传入的参数为：{}",id);
        return moduleService.getModuleById(id);
    }


    /**
     * 根据parentId查询其相邻的子模块
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/querySiblingSubmodules/{id}")
    public Response<?> getModulesByParentId(@PathVariable int id){
        LOG.info("传入的参数为：{}",id);
        return moduleService.getModulesByParentId(id);
    }


    /**
     * 根据id查询所有子模块id
     */
    @CrossOrigin
    @GetMapping("/queryAllSubModules/{id}")
    public Response<?> getSubModulesById(@PathVariable int id){
        LOG.info("传入的参数为：{}",id);
        return moduleService.getSubModulesById(id);
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
    @CrossOrigin
    @GetMapping("queryFormattedModules/{id}")
    public Response<?> getFormattedModules(@PathVariable int id){
        return moduleService.getFormattedModules(id);
    }

    /**
     * 根据parentId和搜索关键字查询子模块
     * @param id
     * @param searchKey
     * @return
     */
    @CrossOrigin
    @GetMapping("/querySiblingSubmodules/")
    public Response<?> getModulesByParentIdAndSearchKey(@RequestParam int id, @RequestParam String searchKey){
        LOG.info("传入的参数为：{}，{}",id,searchKey);
        return moduleService.getModulesByParentIdAndSearchKey(id,searchKey);
    }

    /**
     * 根据模块id获取项目目录树
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/tree/{id}")
    public Response<?> getTreeByModuleId(@PathVariable int id){
        LOG.info("传入参数tests:{}",JacksonUtil.toJson(id));
        return moduleService.getTreeByModuleId(id);
    }

    /**
     * 根据模块id删除模块及其下面的测试用例
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public Response<?> deleteModuleByModuleId(@PathVariable int id){
        LOG.info("传入的参数为：{}",id);
        return moduleService.deleteModuleByModuleId(id);
    }




}

package org.tangxi.testplatform.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.model.log.TestCaseLog;
import org.tangxi.testplatform.service.LogService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class LogController {
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);

    @Autowired
    LogService logService;

    /**
     * 根据测试用例id获取所有测试用例结果
     * @return
     */
    @CrossOrigin
    @GetMapping("/log/{id}")
    public Response<List<TestCaseLog>> getTestCaseLogsById(@PathVariable String id){
        return logService.getTestCaseLogsById(id);
    }

    /**
     * 根据日志id查询日志明细
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/log/info/{id}")
    public Response<List<String>> getTestCaseLogInfoById(@PathVariable String id){
        return logService.getTestCaseLogInfoById(id);
    }

    /**
     * 根据moduleId,reportId分页查询日志
     * @param params
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/")
    public Response<?> getTestCaseLogsByFields(@RequestParam Map<String, Object> params){
        return logService.getTestCaseLogsByFields(params);
    }

}

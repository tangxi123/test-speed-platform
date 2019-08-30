package org.tangxi.testplatform.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.controller.LogController;
import org.tangxi.testplatform.mapper.LogMapper;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.log.TestCaseLog;

import java.util.List;
import java.util.Map;

@Service
public class LogService {
    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);

    @Autowired
    LogMapper logMapper;

    public Response<List<TestCaseLog>> getTestCaseLogsById(String id){
        List<TestCaseLog> testCaseLogsById = logMapper.getTestCaseLogsById(id);
        return new Response<>(200,testCaseLogsById,"查询成功");
    }

    public Response<List<String>> getTestCaseLogInfoById(String id){
        String testCaseLogsById = logMapper.getTestCaseLogInfoById(id);
        List<String> logItems = JacksonUtil.fromJson(testCaseLogsById, new TypeReference<List<String>>() {
        });
        return new Response<>(200,logItems,"查询成功");
    }

    /**
     * 根据moduleId,reportId分页查询日志
     * @param params
     * @return
     */
    public Response<?> getTestCaseLogsByFields(Map<String, Object> params){
        try {
            int pageNum = Integer.parseInt((String) params.get("pageNum"));
            int pageSize = Integer.parseInt((String) params.get("pageSize"));
            PageHelper.startPage(pageNum, pageSize);
            List<TestCaseLog> testCaseLogs = logMapper.getTestCaseLogsByFields(params);
            PageInfo<TestCaseLog> logs = new PageInfo<>(testCaseLogs);
            LOG.info("查询到的测试用例列表为：{}", testCaseLogs);
            LOG.info("返回的分页列表为：{}", logs);
            return new Response<>(200, logs, "查询成功");
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }
}

package org.tangxi.testplatform.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.TestCaseDuplicateException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.service.TestCaseService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/testcases")
public class TestCaseController {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseController.class);

    @Autowired
    TestCaseService testCaseService;

    /**
     * 新增测试用例
     *
     * @param testCase
     * @return
     */
    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    public Response<String> createTestCase(@Valid @RequestBody TestCase testCase) {
        LOG.info("请求的参数为：{}", testCase);
        return testCaseService.createTestCase(testCase);
    }

    /**
     * 修改测试用例
     *
     * @param testCase
     * @return
     */
    @PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
    public Response<String> updateTestCase(@Valid @RequestBody TestCase testCase) {
        LOG.info("请求的参数为：{}", testCase);
        return testCaseService.updateTestCase(testCase);
    }

    /**
     * 根据id获取测试用例
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response<TestCase> getTestcaseById(@PathVariable int id) {
        LOG.info("传入参数id: {}", id);
        return testCaseService.getTestcaseById(id);
    }

    /**
     * 根据测试名字、测试描述分页查询测试用例
     *
     * @param params
     * @return
     */
    @GetMapping("/query/")
    public Response<PageInfo<TestCase>> getTestcases(@RequestBody Map<String, Object> params) {
        LOG.info("传入参数params: {}", JacksonUtil.toJson(params));
        return testCaseService.getTestcases(params);
    }

    /**
     * 根据id删除测试用例
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Response<String> deleteTestCaseById(@PathVariable int id) {
        LOG.info("传入参数id: {}", id);
        return testCaseService.deleteTestCaseById(id);
    }

    /**
     * 根据id禁用测试用例
     *
     * @param id
     * @return
     */
    @GetMapping("/disable/{id}")
    public Response<String> disableTestCaseById(@PathVariable int id) {
        LOG.info("传入参数id: {}", id);
        return testCaseService.disableTestCaseById(id);
    }

    /**
     * 根据id执行测试用例
     * @param id 测试用例id
     * @return
     */
    @GetMapping("/exectest/{id}")
    public Response<?> execTestCaseById(@PathVariable int id){
        LOG.info("传入参数id: {}", id);
        return testCaseService.execTestCaseById(id);
    }

    @GetMapping("/exectest/groups")
    public Response<?> execTestCaseByGroups(@RequestBody Map<String, String> groups){
        LOG.info("传入参数groups: {}", JacksonUtil.toJson(groups));
        return testCaseService.execTestCaseByGroups(groups);
    }

}

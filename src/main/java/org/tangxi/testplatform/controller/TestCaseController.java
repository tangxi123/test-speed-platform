package org.tangxi.testplatform.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.service.TestCaseService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testcases")
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
    @CrossOrigin
    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    public Response<String> createTestCase(@Valid @RequestBody TestCase testCase) {
        LOG.info("请求的参数为：{}", testCase);
        return testCaseService.createTestCase(testCase);
    }

    @CrossOrigin
    @PostMapping(value = "/insertBatchTestCases",produces = "application/json", consumes = "application/json" )
    public Response<String> insertBatchTestCases(@Valid @RequestBody List<TestCase> testCases){
        LOG.info("请求的参数为：{}",JacksonUtil.toJson(testCases));
        return testCaseService.insertBatchTestCases(testCases);
    }

    /**
     * 修改测试用例
     *
     * @param testCase
     * @return
     */
    @CrossOrigin
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
    @CrossOrigin
    @GetMapping("/{id}")
    public Response<TestCase> getTestcaseById(@PathVariable int id) {
        LOG.info("传入参数id: {}", id);
        return testCaseService.getTestcaseById(id);
    }

    /**
     * 根据测试名字、测试描述、模块id分页查询测试用例
     *
     * @param params
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/")
    public Response<PageInfo<TestCase>> getTestcases(@RequestParam Map<String, Object> params) {
        LOG.info("传入参数params: {}", JacksonUtil.toJson(params));
        return testCaseService.getTestcases(params);
    }

    /**
     * 根据id删除测试用例
     *
     * @param id
     * @return
     */
    @CrossOrigin
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
    @CrossOrigin
    @GetMapping("/exectest/{id}")
    public Response<?> execTestCaseById(@PathVariable int id){
        LOG.info("传入参数id: {}", id);
        return testCaseService.execTestCaseById(id);
    }

    /**
     * 根据id列表批量执行测试用例
     * @param tesCaseIds
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/batchExecTestCases",produces = "application/json", consumes = "application/json")
    public Response<String> execTestCaseByIds(@RequestBody Integer[] tesCaseIds){
        LOG.info("传入参数tesCaseIds: {}", JacksonUtil.toJson(tesCaseIds));
        return testCaseService.execTestCaseByIds(tesCaseIds);
    }

    /**
     * 根据模块id执行测试用例
     * @param moduleId
     * @return
     */
    @CrossOrigin
    @GetMapping("/exectestByModuleId/{moduleId}")
    public Response<?> execTestCaseByModuleId(@PathVariable int moduleId){
        return testCaseService.execTestCaseByModuleId(moduleId);
    }

    @GetMapping("/exectest/modules/{id}")
    public Response<?> execTestCaseByGroups(@PathVariable int id){
//        LOG.info("传入参数groups: {}", JacksonUtil.toJson(groups));
        return testCaseService.execTestCaseByGroups(id);
    }

    /**
     * 根据项目（suite）执行测试用例
     * @param suites
     * @return
     */
    @GetMapping("/exectest/tests")
    public Response<?> execTestCaseBySuite(@RequestBody Map<String, String> suites){
        LOG.debug("传入参数tests:{}",JacksonUtil.toJson(suites));
        return testCaseService.execTestCaseByTests(suites);
    }



}

package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.TestCaseDuplicateException;
import org.tangxi.testplatform.common.exception.UnexpectedTestCaseException;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.mapper.PrePostActionMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.TestCase;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestCaseService {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseService.class);

    @Autowired
    TestCaseMapper testCaseMapper;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    PrePostActionMapper prePostActionMapper;

    /**
     * 新增测试用例
     *
     * @param testCase
     * @return
     */
    public Response<String> createTestCase(TestCase testCase) {
        try {
            testCase.setCreatedAt(LocalDateTime.now());
            testCase.setUpdatedAt(testCase.getCreatedAt());
            if(!preActionExists(testCase) || !postActionExists(testCase)){
                return new Response<>(400,null,"有不存在的前后置动作");
            }
            testCaseMapper.createTestCase(testCase);
            return new Response<>(200, null, "测试用例创建成功");
        } catch (DuplicateKeyException e) {
            throw new TestCaseDuplicateException(testCase.getTestName());
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 更新测试用例
     *
     * @param testCase
     * @return
     */
    public Response<String> updateTestCase(TestCase testCase) {
        try {
            testCase.setUpdatedAt(LocalDateTime.now());
            int updateCount = testCaseMapper.updateTestcase(testCase);
            if (updateCount == 1) {
                return new Response<>(200, null, "测试用例更新成功");
            } else {
                return new Response<>(400, null, "测试用例更新失败");
            }
        } catch (DuplicateKeyException e) {
            throw new TestCaseDuplicateException(testCase.getTestName());
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 根据id查询测试用例
     *
     * @param id
     * @return
     */
    public Response<TestCase> getTestcaseById(int id) {
        try {
            TestCase testCase = testCaseMapper.getTestCaseById(id);
            LOG.info("查询到的测试用例为：{}", testCase);
            return new Response<>(200, testCase, "获取测试用例成功");
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 根据测试名字、测试描述分页查询测试用例
     *
     * @param params
     * @return
     */
    public Response<PageInfo<TestCase>> getTestcases(Map<String, Object> params) {
        try {
            int pageNum = (int) params.get("pageNum");
            int pageSize = (int) params.get("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<TestCase> apis = testCaseMapper.getTestCaseByFields(params);
            PageInfo<TestCase> testCases = new PageInfo<>(apis);
            LOG.info("查询到的测试用例列表：{}", apis);
            LOG.info("返回的分页列表为：{}", testCases);
            return new Response<>(200, testCases, "查询成功");
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 根据id删除测试用例
     *
     * @param id
     * @return
     */
    public Response<String> deleteTestCaseById(@PathVariable int id) {
        try {
            int del_count = testCaseMapper.deleteTestCaseById(id);
            if (del_count == 1) {
                return new Response<String>(200, null, "测试用例删除成功");
            } else {
                return new Response<String>(400, null, "测试用例删除失败");
            }
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 根据id禁用测试用例
     *
     * @param id
     * @return
     */
    public Response<String> disableTestCaseById(int id) {
        try {
            int disa_count = testCaseMapper.disableTestCaseById(id);
            if (disa_count == 1) {
                return new Response<String>(200, null, "测试用例禁用成功");
            } else {
                return new Response<String>(400, null, "测试用例禁用失败");
            }
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 根据id执行测试用例
     * @param id 测试用例id
     * @return
     */
    public Response<?> execTestCaseById(int id){
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
        TestNG testng = new TestNG();
        TestCase testCase = testCaseMapper.getTestCaseById(id);
        XmlSuite suite = new XmlSuite();
        suite.setParameters(params);
        suite.setName(testCase.getSuite());
        XmlTest test = new XmlTest(suite);
        test.setName("parameter接口相关测试");
//        test.setParameters(params);
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass("org.tangxi.testcase.execution.TestApplication"));
        test.setXmlClasses(classes);
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        testng.setXmlSuites(suites);
        testng.run();
        return null;
    }

    /**
     * 根据子模块（group）执行测试用例
     * @param groups
     * @return
     */
    public Response<?> execTestCaseByGroups(Map<String, String> groups){
        String group = (String)groups.get("groups");
        TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
        TestNG testng = new TestNG();
        List<TestCase> testCases = testCaseMapper.getTestCasesByGroups(group);
        XmlSuite suite = new XmlSuite();
        suite.setName("testPlatform-api");
        XmlTest test = new XmlTest();
        test.setName("parameter接口相关测试");
        XmlGroups xmlGroups = new XmlGroups();
        XmlRun xmlRun = new XmlRun();
        xmlRun.onInclude(group);
        xmlGroups.setRun(xmlRun);
        test.setGroups(xmlGroups);
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass("org.tangxi.testcase.execution.TestApplication"));
        test.setXmlClasses(classes);
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        testng.setXmlSuites(suites);
        testng.run();
        return null;
    }

    private int getParamCount(TestCase testCase){
        String parameters = testCase.getParameters();
        int paramCount = parameterMapper.getParamCountByName(parameters);
        return paramCount;
    }

    private boolean preActionExists(TestCase testCase){
        List<String> preActionNames = testCase.getPreActionNames();
        for(String name : preActionNames){
            int actionCount = prePostActionMapper.getActionCountByName(name);
            if(actionCount < 1){
                return false;
            }
        }
        return true;
    }

    private boolean postActionExists(TestCase testCase){
        List<String> postActionNames = testCase.getPostActionNames();
        for(String name : postActionNames){
            int actionCount = prePostActionMapper.getActionCountByName(name);
            if(actionCount < 1){
                return false;
            }
        }
        return true;
    }
}






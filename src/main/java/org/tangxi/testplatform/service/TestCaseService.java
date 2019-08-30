package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jayway.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.UnexpectedModuleException;
import org.tangxi.testplatform.common.exception.testcase.*;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.execution.TestExecutionById;
import org.tangxi.testplatform.execution.steps.logs.RecordLogs;
import org.tangxi.testplatform.mapper.*;
import org.tangxi.testplatform.model.Module;
import org.tangxi.testplatform.model.ModuleTree;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.log.ModuleMapLog;
import org.tangxi.testplatform.model.log.ModuleMapLogTree;
import org.tangxi.testplatform.model.log.TestCaseLog;
import org.tangxi.testplatform.model.testReport.Report;
import org.tangxi.testplatform.model.testReport.ReportInfo;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TestCaseService {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseService.class);


    private static final int RUN_FAIL_STATUS = 0; //测试失败
    private static final int RUN_SUCCESS_STATUS = 1; //测试成功
    private static final int NO_TESTCASE_STATUS = 2; //没有测试用例


    @Autowired
    TestCaseMapper testCaseMapper;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    PrePostActionMapper prePostActionMapper;

    @Autowired
    ModuleMapper moduleMapper;

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    TestExecutionById testExecutionById;


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
            testCaseMapper.createTestCase(testCase);
            return new Response<>(200, null, "测试用例创建成功");
        } catch (DuplicateKeyException e) {
            throw new TestCaseDuplicateException(testCase.getTestName());
        } catch (Exception e) {
            throw new UnexpectedTestCaseException(e);
        }
    }

    /**
     * 批量增加测试用例
     *
     * @param testCases
     * @return
     */
    public Response<String> insertBatchTestCases(List<TestCase> testCases) {
        try {
            for (TestCase testCase : testCases) {
                testCase.setCreatedAt(LocalDateTime.now());
                testCase.setUpdatedAt(testCase.getCreatedAt());
            }
            testCaseMapper.insertBatchTestCases(testCases);
            return new Response<>(200, null, "批量创建测试用例成功");
        } catch (DuplicateKeyException e) {
            throw new TestCaseDuplicateException(e.getMessage());
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
     * 根据测试名字、测试描述、模块id分页查询测试用例
     *
     * @param params
     * @return
     */
    public Response<PageInfo<TestCase>> getTestcases(Map<String, Object> params) {
        try {
            int pageNum = Integer.parseInt((String) params.get("pageNum"));
            int pageSize = Integer.parseInt((String) params.get("pageSize"));
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
     *
     * @param id 测试用例id
     * @return
     */
    public Response<?> execTestCaseById(int id) {
        testExecutionById.run(id);
        return new Response<>(200, null, "测试用例执行完成");
    }

    /**
     * 根据id列表批量执行测试用例
     *
     * @param ids
     * @return
     */
    public Response<String> execTestCaseByIds(Integer[] ids) {
        List<String> exceptions = new ArrayList<>();
        for (Integer id : ids) {
            try {
                testExecutionById.run(id);
            } catch (Exception e) {
                exceptions.add(e.getMessage());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(outputStream));
                String exceptionString = outputStream.toString();
                LOG.error(exceptionString);
            }
        }
        if (!exceptions.isEmpty()) {
            return new Response<>(400, null, JacksonUtil.toJson(exceptions));
        }
        return new Response<>(200, null, "测试执行完成");
    }

    /**
     * 根据模块id执行测试用例
     *
     * @param moduleId
     * @return
     */
    public Response<?> execTestCaseByModuleId(int moduleId) {
        List<String> exceptions = new ArrayList<>();
        Module module = moduleMapper.getModuleById(moduleId);
        if (module == null) {
            throw new UnexpectedTestCaseException("生成测试报告失败：没有该模块" + moduleId);
        }
        Map<String, Object> moduleMap = new HashMap<>();
        moduleMap.put("moduleId", moduleId);
        String reportId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        List<TestCase> testCases = testCaseMapper.getTestCaseByFields(moduleMap);
        if (testCases == null) {
            throw new UnexpectedTestCaseException("生成测试报告失败：没有该模块" + moduleId);
        }
        for (TestCase testCase : testCases) {
            int id = Integer.parseInt(String.valueOf(testCase.getId()));
            try {
                testExecutionById.run(id);
            } catch (Exception e) {
                exceptions.add(e.getMessage());
                e.printStackTrace();
            }
        }

        //更新测试用例日志的reportId
        updateTestCaseLogIdsReportId(reportId,testCases);
        //写入测试总报告
        writeMainReport(moduleId,reportId);
        //写入各模块测试结果
        writeModuleReport(moduleId,reportId);
        if (!exceptions.isEmpty()) {
            return new Response<>(400, null, JacksonUtil.toJson(exceptions));
        }
        return new Response<>(200, null, "测试执行完成");
    }

    /**
     * 更新测试用例日志的reportId
     *
     * @param reportId
     */
    private void updateTestCaseLogIdsReportId(String reportId,List<TestCase> testCases) {
        //更新测试用例日志的reportId
        List<Integer> testCaseLogIds = new ArrayList<>();
        testCaseLogIds.addAll(RecordLogs.testCaseLogIds);
        for (int testCaseLogId : testCaseLogIds) {
            TestCaseLog testCaseLog = logMapper.getTestCaseLogById("" + testCaseLogId);
            testCaseLog.setReportId(reportId);
            for(TestCase testCase : testCases){
                if(testCase.getId() == testCaseLog.getTestCaseId()){
                    testCaseLog.setModuleId(testCase.getModuleId());
                }
            }
            logMapper.updateLog(testCaseLog); //更新测试用例的模块id和报告id
        }
    }

    /**
     * 写入测试总报告
     *
     * @param reportId
     */
    private void writeMainReport(int moduleId,String reportId) {
        List<Integer> testCaseLogIds = new ArrayList<>();
        testCaseLogIds.addAll(RecordLogs.testCaseLogIds);
        RecordLogs.testCaseLogIds.clear();
        Report report = new Report();
        report.setId(reportId);
        report.setModuleId("" + moduleId);
        report.setReportName(moduleMapper.getModuleById(moduleId).getName() + "_" + LocalDateTime.now());
        int report_passed_count = 0;
        int report_failed_count = 0;
        for (int testCaseLogId : testCaseLogIds) {
            TestCaseLog testCaseLog = logMapper.getTestCaseLogById("" + testCaseLogId);
            report_passed_count = report_passed_count + testCaseLog.getPassedTcCount();
            report_failed_count = report_failed_count + testCaseLog.getFailedTcCount();
        }
        report.setPassedTcCount(report_passed_count);
        report.setFailedTcCount(report_failed_count);
        report.setTestResultStatus(getResultStatus(report_passed_count, report_failed_count));
        report.setReportTime(LocalDateTime.now());
        reportMapper.insertReport(report);
    }

    private void writeModuleReport(int moduleId,String reportId) {
        List<Module> subModules = moduleMapper.getSubModulesById(moduleId);
        List<ModuleMapLog> moduleMapLogs = new ArrayList<>();
        for(Module submodule : subModules){
            int subModuleId = submodule.getId();
            Map<String, Object> subModuleMap = new HashMap<>();
            subModuleMap.put("moduleId", subModuleId);
            subModuleMap.put("reportId",reportId);
            List<TestCaseLog> testCaseLogs = logMapper.getTestCaseLogsByFields(subModuleMap);
            ModuleMapLog moduleMapLog = new ModuleMapLog();
            int passCount = 0;
            int failCount = 0;
            int resultStatus = 3;
            for(TestCaseLog testCaseLog : testCaseLogs){
                passCount = passCount + testCaseLog.getPassedTcCount();
                failCount = failCount + testCaseLog.getFailedTcCount();
                resultStatus = getResultStatus(passCount, failCount);
            }
            moduleMapLog.setModuleId(subModuleId);
            moduleMapLog.setName(submodule.getName());
            moduleMapLog.setParentModuleId(submodule.getParentId());
            moduleMapLog.setPassedCount(passCount);
            moduleMapLog.setFailedCount(failCount);
            moduleMapLog.setTestResultStatus(resultStatus);
            moduleMapLog.setReportId(reportId);
            moduleMapLogs.add(moduleMapLog);
        }
        logMapper.insertModuleMapLog(moduleMapLogs);


    }

    /**
     * 获取测试用例是通过还是失败
     * @param pass_count
     * @param fail_count
     * @return
     */
    private int getResultStatus(int pass_count, int fail_count) {
        if (fail_count > 0) {
            return RUN_FAIL_STATUS;
        } else if (fail_count == 0 && pass_count > 0) {
            return RUN_SUCCESS_STATUS;
        } else {
            return NO_TESTCASE_STATUS;
        }
    }




    /**
     * 写入report数据
     *
     * @param moduleId
     * @return
     */
    private Report recordReport(int moduleId) {
        HashMap<String, Object> moduleMap = new HashMap<>();
        moduleMap.put("moduleId", moduleId);
        int success_count = 0;
        int fail_count = 0;
        List<TestCase> executedTestCase = testCaseMapper.getTestCaseByFields(moduleMap);
        if (executedTestCase == null) {
            throw new TestCaseNotFoundException("该模块下没有测试用例");
        }
        for (TestCase testCase1 : executedTestCase) {
            if (testCase1.getIsPassed() == RUN_SUCCESS_STATUS) {
                success_count++;
            } else {
                fail_count++;
            }

        }
        Report report = new Report();
        report.setModuleId("" + moduleId);
        Module module = moduleMapper.getModuleById(moduleId);
        if (module == null) {
            throw new UnexpectedTestCaseException("生成测试报告失败：没有该模块" + moduleId);
        }
        report.setReportName(moduleMapper.getModuleById(moduleId).getName() + "_" + LocalDateTime.now());
        report.setPassedTcCount(success_count);
        report.setFailedTcCount(fail_count);
        List<Integer> testCaseLogIds = RecordLogs.testCaseLogIds;

        //获取直接属于该产品的测试用例
        List<Module> subModules = moduleMapper.getSubModulesById(moduleId);
        List<Integer> subModuleIds = new ArrayList<>();
        for (Module subModule : subModules) {
            int subModuleId = subModule.getId();
            subModuleIds.add(subModuleId);
        }
        subModuleIds.remove((Integer) moduleId); //移除当前的模块id
//        if(subModuleIds.size() == 0){
//
//        }
        List<Integer> testCaseLogIdsBeLongToModule = new ArrayList<>();
        testCaseLogIdsBeLongToModule.addAll(testCaseLogIds);

        for (Integer testCaseLogId : testCaseLogIds) {
            TestCaseLog testCaseLog = logMapper.getTestCaseLogById("" + testCaseLogId);
            int testCaseId = testCaseLog.getTestCaseId();
            TestCase testCase = testCaseMapper.getTestCaseById(testCaseId);
            int testCaseModuleId = testCase.getModuleId();
            if (subModuleIds.contains(testCaseModuleId)) {
                testCaseLogIdsBeLongToModule.remove(testCaseLogId);
            }
        }

        report.setTestCaseLogIds(testCaseLogIdsBeLongToModule);
        if (fail_count > 0) {
            report.setTestResultStatus(RUN_FAIL_STATUS);
        } else if (fail_count == 0 && success_count > 0) {
            report.setTestResultStatus(RUN_SUCCESS_STATUS);
        } else if (fail_count == 0 && success_count == 0) {
            report.setTestResultStatus(NO_TESTCASE_STATUS);
        }
        reportMapper.insertReport(report);

//
//        //插入模块报告明细
//        List<Module> subSiblingModules = moduleMapper.getModulesByParentId(moduleId);
//
//        for(Module subSiblingModule : subSiblingModules){
//            List<Module> subSubModules = moduleMapper.getSubModulesById(subSiblingModule.getId());
//            List<Integer> subSubModuleIds = new ArrayList<>();
//            for(Module subSubModule : subSubModules){
//                subSubModuleIds.add(subSubModule.getId());
//            }
//
//        }

        List<ReportInfo> reportInfos = new ArrayList<>();
        return report;
    }

    private void recordReportInfos(int moduleId, Report report) {
        List<Module> subModules = moduleMapper.getModulesByParentId(
                moduleId);
        List<ReportInfo> reportInfos = new ArrayList<>();
        if (subModules.size() < 1) {
            return;
        }
        for (Module subModule : subModules) {
            int mId = subModule.getId();
            HashMap<String, Object> moduleM = new HashMap<>();
            moduleM.put("moduleId", mId);
            List<TestCase> testCasem = testCaseMapper.getTestCaseByFields(moduleM);
            int m_success_count = 0;
            int m_fail_count = 0;
            for (TestCase testCase1 : testCasem) {
                if (testCase1.getIsPassed() == RUN_SUCCESS_STATUS) {
                    m_success_count++;
                } else {
                    m_fail_count++;
                }

            }
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setReportId(report.getId());
            reportInfo.setModuleName(moduleMapper.getModuleById(mId).getName());
            reportInfo.setModuleId(moduleMapper.getModuleById(mId).getId());
            reportInfo.setPassedTcCount(m_success_count);
            reportInfo.setFailedTcCount(m_fail_count);
            if (m_fail_count > 0) {
                reportInfo.setTestResultStatus(RUN_FAIL_STATUS);
            } else if (m_fail_count == 0 && m_success_count > 0) {
                reportInfo.setTestResultStatus(RUN_SUCCESS_STATUS);
            } else if (m_fail_count == 0 && m_success_count == 0) {
                reportInfo.setTestResultStatus(NO_TESTCASE_STATUS);
            }
            reportInfos.add(reportInfo);
        }
        reportMapper.insertReportInfo(reportInfos);
    }


    /**
     * 根据子模块（group）执行测试用例
     *
     * @param id
     * @return
     */
    public Response<?> execTestCaseByGroups(int id) {

        HashMap<String, String> suiteIdMap = new HashMap<>();  //将moduleId设置为testNG配置文件中的参数
        suiteIdMap.put("suiteId", String.valueOf(id));
        String moduleName = moduleMapper.getModuleById(id).getName();  //根据id获取模块名字

        List<Module> subModules = moduleMapper.getSubModulesById(id);
        System.out.println(JacksonUtil.toJson(subModules));

        TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
        TestNG testng = new TestNG();

        XmlSuite suite = new XmlSuite();   //设置suite
        suite.setParameters(suiteIdMap);  //设置suite参数为suiteId
        suite.setName(moduleName);
        suite.addListener("org.tangxi.testcase.execution.testReport.ReportListener");
        for (Module module : subModules) {
            int mId = module.getId();
            String name = module.getName();
            HashMap<String, String> mIdMap = new HashMap<>();  //将moduleId设置为testNG配置文件中的参数
            mIdMap.put("moduleId", String.valueOf(mId));

            XmlTest test = new XmlTest(suite);
            test.setName(name + mId);
            test.setParameters(mIdMap);
            List<XmlClass> classes = new ArrayList<>();
            classes.add(new XmlClass("org.tangxi.testcase.execution.TestApplicationSuite"));
            test.setXmlClasses(classes);
        }
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        testng.setXmlSuites(suites);
        testng.run();
        return null;
    }

    /**
     * 根据项目（suite）执行测试用例
     *
     * @param projectName
     * @return
     */
    public Response<?> execTestCaseByTests(Map<String, String> projectName) {
        TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
        TestNG testng = new TestNG();

        XmlSuite suite = new XmlSuite();
        suite.setParameters(projectName);
        suite.setName(projectName.get("suite"));
        XmlTest test = new XmlTest(suite);
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass("org.tangxi.testcase.execution.TestApplicationSuite"));
        test.setXmlClasses(classes);
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        testng.setXmlSuites(suites);
        testng.run();
        return null;
    }

    private int getParamCount(TestCase testCase) {
        String parameters = testCase.getParameters();
        int paramCount = parameterMapper.getParamCountByName(parameters);
        return paramCount;
    }

    private boolean preActionExists(TestCase testCase) {
        List<String> preActionNames = testCase.getPreActionNames();
        for (String name : preActionNames) {
            int actionCount = prePostActionMapper.getActionCountByName(name);
            if (actionCount < 1) {
                return false;
            }
        }
        return true;
    }

    private boolean postActionExists(TestCase testCase) {
        List<String> postActionNames = testCase.getPostActionNames();
        for (String name : postActionNames) {
            int actionCount = prePostActionMapper.getActionCountByName(name);
            if (actionCount < 1) {
                return false;
            }
        }
        return true;
    }
}






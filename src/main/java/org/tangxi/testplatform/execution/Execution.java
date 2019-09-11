package org.tangxi.testplatform.execution;

import com.jayway.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseAssertionError;
import org.tangxi.testplatform.common.exception.testcase.TestCaseNotFoundException;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.aop.ExecutionTime;
import org.tangxi.testplatform.execution.aop.LogAspect;
import org.tangxi.testplatform.execution.steps.actions.ActionExecution;
import org.tangxi.testplatform.execution.steps.logs.RecordLogs;
import org.tangxi.testplatform.execution.steps.replace.ReplaceHolder;
import org.tangxi.testplatform.execution.steps.replace.ReplaceTestCase;
import org.tangxi.testplatform.execution.steps.requests.ReplaceSelectParam;
import org.tangxi.testplatform.execution.steps.requests.RequestExecution;
import org.tangxi.testplatform.execution.steps.verifys.VerifyExecution;
import org.tangxi.testplatform.mapper.LogMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.log.TestCaseLog;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Execution {
    private static final Logger LOG = LoggerFactory.getLogger(Execution.class);
    public static final List<String> logs = new ArrayList<>();

    private static final String PRE = "pre.";
    private static final String POST = "post.";

    private static final int RUN_SUCCESS_STATUS = 1;
    private static final int RUN_EXCEPTION_STATUS = 2;
    private static final int ASSERT_ERROR_STATUS = 3;
    private static final int UNEXPECTED_EXCEPTION_STATUS = 4;

    private TestCase testCase;

    private TestCaseWrapper testCaseWrapper;

    private JsonPath requestResult;

    private List<String> checkResults;


    @Autowired
    TestCaseMapper testCaseMapper;

    @Autowired
    ReplaceTestCase replaceTestCase;

    @Autowired
    ActionExecution actionExecution;

    @Autowired
    ReplaceSelectParam replaceSelectParam;

    @Autowired
    RequestExecution requestExecution;

    @Autowired
    VerifyExecution verifyExecution;

    @Autowired
    LogMapper logMapper;

    @Autowired
    LogAspect logAspect;

    @Autowired
    RecordLogs recordLogs;


    public void run(int id) {
        try {
            Execution.logs.add("执行的测试用例id为：" + id);
            List<PrePostActionWrapper> preActionWrappers = new ArrayList<>();
            testCase = testCaseMapper.getTestCaseById(id);
            Execution.logs.add("执行的测试用例名字为："+testCase.getTestName());
            if (testCase == null) {
                Execution.logs.add("不存在的测试用例id");
                throw new TestCaseNotFoundException("不存在的测试用例id");
            }
            testCaseWrapper = replaceTestCase.generateReplacedTestCaseWrapper(testCase);//替换参数并生成testCaseWrapper
            preActionWrappers = testCaseWrapper.getPreActions();//执行前置动作
            actionExecution.execPreActions(preActionWrappers);
            testCaseWrapper = replaceSelectParam.replaceTestCaseWrapper(testCaseWrapper, PRE);//替换testCaseWrapper前置动作参数并返回testcaseWrapper
            requestResult = requestExecution.sendRequest(testCaseWrapper);//发起请求
            verifyExecution.execVerifyCheckPoint(requestResult, testCaseWrapper);//校验检查点
            testCaseMapper.updateTestCaseResult(testCase.getId(), JacksonUtil.toJson(checkResults), RUN_SUCCESS_STATUS);//将校验结果写入数据库中
        } catch (TestCaseNotFoundException e) {
            throw new TestCaseNotFoundException(e.getMessage());
        } catch (TestCaseRunException e) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            String exceptionString = outputStream.toString();
            Execution.logs.add("运行测试用例错误："+exceptionString);
            checkResults = testCaseWrapper.getCheckResult();
            testCaseMapper.updateTestCaseResult(testCase.getId(), JacksonUtil.toJson(checkResults), RUN_EXCEPTION_STATUS);
            throw new TestCaseRunException(e.getMessage());
        } catch (TestCaseAssertionError e) {
            Execution.logs.add("【检查点校验】断言错误："+e.getMessage());
            checkResults = testCaseWrapper.getCheckResult();
            testCaseMapper.updateTestCaseResult(testCase.getId(), JacksonUtil.toJson(checkResults), ASSERT_ERROR_STATUS);
            throw new TestCaseAssertionError(e.getMessage());
        } catch (Exception e) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            String exceptionString = outputStream.toString();
            Execution.logs.add("异常错误："+exceptionString);
            String message = e.toString();
            testCaseMapper.updateTestCaseResult(testCase.getId(), message, UNEXPECTED_EXCEPTION_STATUS);
            throw new UnexpectedTestCaseException(e.getMessage());
        } finally {
            try{
                List<PrePostActionWrapper> postActionWrappers = new ArrayList<>();
                postActionWrappers = testCaseWrapper.getPostActions();//执行后置动作
                actionExecution.execPostActions(postActionWrappers);
                testCaseWrapper = replaceSelectParam.replaceTestCaseWrapper(testCaseWrapper, POST); //替换testCaseWrapper后置动作参数并返回testcaseWrapper
                checkResults = testCaseWrapper.getCheckResult();//获取测试用例校验结果
            }catch (Exception e){
                throw new UnexpectedTestCaseException(e.getMessage());
            }finally {
                Execution.logs.add("测试用例执行完成");
                recordLogs.recordLogs(id);
                //清理掉这一次的动作参数
                testCaseWrapper.clearSelectPreParams();
                //清理掉这一次的检查结果
                testCaseWrapper.setCheckResult(new ArrayList<>());
            }

        }
    }
}

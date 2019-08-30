package org.tangxi.testplatform.execution.steps.verifys;

import com.jayway.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseAssertionError;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;



@Component
public class VerifyExecution {
    private static final Logger LOG = LoggerFactory.getLogger(VerifyExecution.class);

    /**
     * 验证检查点
     * @param requestResult
     * @param testCaseWrapper
     */
    public void  execVerifyCheckPoint(JsonPath requestResult, TestCaseWrapper testCaseWrapper){
        TestCase testCase = testCaseWrapper.getTestCase();
        List<CheckPoint> checkPoints = testCase.getCheckPoints();
        List<String> checkResults = new ArrayList<>();
        SoftAssert assertion = new SoftAssert();
        Execution.logs.add("开始执行【检查点校验】");
        for(CheckPoint checkPoint : checkPoints){
            Execution.logs.add("【检查点校验】检查点为："+JacksonUtil.toJson(checkPoint));
            String checkResult = VerifyUtil.verifyCheck(requestResult,checkPoint,assertion);
            if(checkResult == null){
                throw new TestCaseAssertionError("校验结果为空");
            }
            Execution.logs.add("【检查点校验】结果为："+checkResult);
            checkResults.add(checkResult);
        }
        testCaseWrapper.setCheckResult(checkResults);
        try {
            assertion.assertAll();
        }catch (AssertionError e){
            throw new TestCaseAssertionError(e);
        }
    }

    /**
     * 执行验证
     * @param requestResult
     * @param checkPoint
     * @param assertion
     */
    private void verifyCheck(JsonPath requestResult,CheckPoint checkPoint, SoftAssert assertion){

    }
}

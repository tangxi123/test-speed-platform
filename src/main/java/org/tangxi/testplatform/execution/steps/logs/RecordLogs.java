package org.tangxi.testplatform.execution.steps.logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.execution.steps.replace.ReplaceAction;
import org.tangxi.testplatform.execution.steps.replace.ReplaceCheckPoint;
import org.tangxi.testplatform.execution.steps.replace.ReplaceHolder;
import org.tangxi.testplatform.execution.steps.replace.ReplaceTestCase;
import org.tangxi.testplatform.mapper.LogMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.log.TestCaseLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RecordLogs {
    private static final List<String> logs = new ArrayList<>();
    public static final List<Integer> testCaseLogIds = new ArrayList<>();

    private static final int RUN_FAIL_STATUS = 0; //测试失败
    private static final int RUN_SUCCESS_STATUS = 1; //测试成功
    private static final int NO_TESTCASE_STATUS = 2; //没有测试用例

    @Autowired
    LogMapper logMapper;

    @Autowired
    TestCaseMapper testCaseMapper;


    public void recordLogs(int testCaseId){
        logs.addAll(Execution.logs);
        TestCase testCase = testCaseMapper.getTestCaseById(testCaseId);
        LocalDateTime now = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String logName = testCase.getTestName()+"_"+testCase.getId()+"_"+now+"_"+uuid+"日志";
        TestCaseLog testCaseLog = new TestCaseLog();
        testCaseLog.setTestCaseId(testCaseId);
        testCaseLog.setTestCaseName(testCase.getTestName());
        testCaseLog.setName(logName);
        testCaseLog.setLogs(logs);
        testCaseLog.setExecutionTime(testCase.getExecutionTime());
        testCaseLog.setResult(testCase.getResult());
        if(testCase.getIsPassed() == 1){
            testCaseLog.setPassedTcCount(1);
            testCaseLog.setFailedTcCount(0);
            testCaseLog.setTestResultStatus(RUN_SUCCESS_STATUS);
        }else if(testCase.getIsPassed() == 2 || testCase.getIsPassed() == 3 || testCase.getIsPassed() == 4){
            testCaseLog.setPassedTcCount(0);
            testCaseLog.setFailedTcCount(1);
            testCaseLog.setTestResultStatus(RUN_FAIL_STATUS);
        }else{
            testCaseLog.setPassedTcCount(0);
            testCaseLog.setFailedTcCount(0);
            testCaseLog.setTestResultStatus(NO_TESTCASE_STATUS);
        }

        logMapper.insertLog(testCaseLog);
        TestCaseLog testCaseLogByName = logMapper.getTestCaseLogByName(logName);
        int testCaseLogId = testCaseLogByName.getId();
        testCaseLogIds.add(testCaseLogId);
        clearLastLogs();
    }

    private void clearLastLogs(){
        //清理掉这一次的日志
        Execution.logs.clear();
        logs.clear();
    }
}

package org.tangxi.testplatform.execution;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.tangxi.testplatform.TestSpeedPlatformApplication;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.aop.ExecutionTime;
import org.tangxi.testplatform.execution.aop.ModuleCheck;
import org.tangxi.testplatform.execution.steps.actions.ActionExecution;
import org.tangxi.testplatform.execution.steps.replace.ReplaceTestCase;
import org.tangxi.testplatform.execution.steps.requests.ReplaceSelectParam;
import org.tangxi.testplatform.execution.steps.requests.RequestExecution;
import org.tangxi.testplatform.execution.steps.verifys.VerifyExecution;
import org.tangxi.testplatform.mapper.ExecuteSqlMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.TestCase;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

@Component
public class TestExecutionById {
    private static final Logger LOG = LoggerFactory.getLogger(TestExecutionById.class);

    @Autowired
    Execution execution;

    @ExecutionTime
    public void run(int id){
        execution.run(id);
    }







}

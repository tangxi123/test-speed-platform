package org.tangxi.testplatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.TestCaseDuplicateException;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.service.TestCaseService;

@RestController
public class TestCaseController {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseController.class);

    @Autowired
    TestCaseService testCaseService;

    @PostMapping(value = "/testcases/create", produces = "application/json", consumes = "application/json")
    public Response<String> createTestCase(@RequestBody TestCase testCase){
        LOG.info("请求的参数为：{}",testCase);
        return testCaseService.createTestCase(testCase);
    }


}

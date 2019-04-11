package org.tangxi.testplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.controller.TestCaseController;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.TestCase;

@Service
public class TestCaseService {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseService.class);

    @Autowired
    TestCaseMapper testCaseMapper;

    public Response<String> createTestCase(TestCase testCase){
        testCaseMapper.createTestCase(testCase);
        return null;
    }
}

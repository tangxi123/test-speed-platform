package org.tangxi.testplatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.TestCaseDuplicateException;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
    /**
     * 测试用例名字重复的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(TestCaseDuplicateException.class)
    public Response<String> testCaseNameDuplicate(TestCaseDuplicateException e){
        String testName = e.getTestName();
        LOG.info("测试用例名字{}重复，请重新输入",testName);
        return new Response<>(400,null,"测试用例名字"+testName+"重复，请重新输入");
    }
}

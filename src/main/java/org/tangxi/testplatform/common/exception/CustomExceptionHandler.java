package org.tangxi.testplatform.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.tangxi.testplatform.common.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * 测试用例名字重复的异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(TestCaseDuplicateException.class)
    public Response<String> testCaseNameDuplicate(TestCaseDuplicateException e) {
        String testName = e.getTestName();
        LOG.info("测试用例名字{}重复，请重新输入", testName);
        return new Response<>(400, null, "测试用例名字:" + testName + "重复，请重新输入");
    }

    /**
     * 无法预测的测试用例异常错误的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedTestCaseException.class)
    public Response<String> testCaseUnexpectedError(UnexpectedTestCaseException e) {
        LOG.error(e.getMessage());
        return new Response<>(500, null, "服务器错误");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    Response<String> onConstraintValidationException(ConstraintViolationException e) {

        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            errorMessages.add(violation.getMessage());

        }
        LOG.info(errorMessages.toString());
        return new Response<String>(400, null, errorMessages.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    Response<String> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        LOG.info(errorMessages.toString());
        return new Response<String>(400, null, errorMessages.toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    Response<String> onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOG.info(e.getMessage());
        return new Response<String>(400, null, e.getMessage());
    }

    /**
     * 前后置动作名字重复的异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ActionDuplicateException.class)
    public Response<String> actionNameDuplicate(ActionDuplicateException e) {
        String testName = e.getActionName();
        LOG.info("前后置动作名字{}重复，请重新输入", testName);
        return new Response<>(400, null, "前后置动作名字:" + testName + "重复，请重新输入");
    }

    /**
     * 无法预测的前后置动作异常错误的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedActionException.class)
    public Response<String> actionUnexpectedError(UnexpectedActionException e) {
        LOG.error(e.getMessage());
        return new Response<>(500, null, "服务器错误");
    }

}

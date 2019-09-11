package org.tangxi.testplatform.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.testcase.*;
import org.tangxi.testplatform.execution.Execution;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
     * 执行测试用例时的错误处理
     * @return
     */
    @ExceptionHandler(TestCaseRunException.class)
    public Response<?> testCaseRunError(TestCaseRunException e){
        LOG.error(e.getMessage());
//        Execution.logs.add("错误："+e.getMessage());
//        throw new TestCaseRunException(e);
        return new Response<>(400,null,e.getMessage());
    }

    /**
     * 无法预测的测试用例异常错误的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedTestCaseException.class)
    public Response<?> testCaseUnexpectedError(UnexpectedTestCaseException e){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(outputStream));
        String exceptionString = outputStream.toString();
        LOG.error(exceptionString);
//        throw new UnexpectedTestCaseException(e);
        return new Response<>(500, null, e.getMessage());
    }

    /**
     * 测试用例断言时发生的错误处理
     * @param e
     * @return
     */
//    @ExceptionHandler(TestCaseAssertionError.class)
//    public Response<String> testCaseAssertionError(TestCaseAssertionError e){
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        e.printStackTrace(new PrintStream(outputStream));
//        String exceptionString = outputStream.toString();
//        LOG.error(exceptionString);
//
//        return new Response<>(400, null, e.getMessage());
//    }

    @ExceptionHandler(TestCaseAssertionError.class)
    public Response<?> testCaseAssertionError(TestCaseAssertionError e){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(outputStream));
        String exceptionString = outputStream.toString();
        LOG.error(exceptionString);
        return new Response<>(400, null, e.getMessage());
    }

    /**
     * 测试用例不存在的错误处理
     * @param e
     * @return
     */
    @ExceptionHandler(TestCaseNotFoundException.class)
    public Response<?> testCaseNotFoundException(TestCaseNotFoundException e){
        LOG.error(e.getMessage());
//        Execution.logs.add("错误："+e.getMessage());
//        throw new TestCaseNotFoundException(e);
        return new Response<>(400,null,e.getMessage());
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

    /**
     * 参数名字重复的异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ParamDuplicateException.class)
    public Response<String> paramNameDuplicate(ParamDuplicateException e) {
        String name = e.getName();
        LOG.info("参数名字{}重复，请重新输入", name);
        return new Response<>(400, null, "参数名字:" + name + "重复，请重新输入");
    }

    /**
     * 无法预测的参数异常错误的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedParamException.class)
    public Response<String> paramUnexpectedError(UnexpectedParamException e) {
        LOG.error(e.getMessage());
        return new Response<>(500, null, "服务器错误");
    }

    /**
     * 无法预测的模块异常错误的处理
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedModuleException.class)
    public Response<String> moduleUnexpectedError(UnexpectedModuleException e){
        LOG.error(e.getMessage());
        return new Response<>(500,null,"服务器错误");
    }

    /**
     * 无法预测的url异常错误的处理
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedUrlException.class)
    public Response<String> urlUnexpectedError(UnexpectedUrlException e){
        LOG.error(e.getMessage());
        return new Response<>(500,null,"服务器错误");
    }

    /**
     * 无法预测的报告错误
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedReportException.class)
    public Response<String> reportUnexpectedError(UnexpectedReportException e){
        LOG.error(e.getMessage());
        return new Response<>(500,null,"服务器错误");
    }

    /**
     * 无法预测的数据库配置错误
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedDatabaseConfigException.class)
    public Response<String> databaseConfigUnexpectedError(UnexpectedDatabaseConfigException e){
        LOG.error(e.getMessage());
        return new Response<>(500,null,"服务器错误");
    }
}

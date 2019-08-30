package org.tangxi.testplatform.execution.steps.requests;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.model.TestCase;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.head;

@Component
public class RequestExecution {
    private static final Logger LOG = LoggerFactory.getLogger(RequestExecution.class);
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    /**
     * 发起http请求
     * @param testCaseWrapper
     * @return
     */
    public JsonPath sendRequest(TestCaseWrapper testCaseWrapper){
        Execution.logs.add("【发起Web请求】开始【发送http请求】");
        JsonPath requestResult;
        RequestSpecification spec = initSpec(testCaseWrapper);
        TestCase testCase = testCaseWrapper.getTestCase();
        String method = testCase.getMethod();
        switch (method){
            case GET:
                requestResult = sendGetRequest(testCaseWrapper, spec);
                Execution.logs.add("【发起Web请求】返回结果为："+requestResult.prettyPrint());
                return requestResult;
            case PUT:
                requestResult = sendPutRequest(testCaseWrapper, spec);
                Execution.logs.add("【发起Web请求】返回结果为："+requestResult.prettyPrint());
                return requestResult;
            case POST:
                requestResult = sendPostRequest(testCaseWrapper, spec);
                Execution.logs.add("【发起Web请求】返回结果为："+requestResult.prettyPrint());
                return requestResult;
            case DELETE:
                requestResult = sendDeleteRequest(testCaseWrapper, spec);
                Execution.logs.add("【发起Web请求】返回结果为："+requestResult.prettyPrint());
                return requestResult;
            default:
                throw new UnexpectedTestCaseException("不支持的http请求方式："+method);
        }
    }

    /**
     * 发起http的GET请求
     * @param testCaseWrapper
     * @param spec
     * @return
     */
    private JsonPath sendGetRequest(TestCaseWrapper testCaseWrapper, RequestSpecification spec){

        TestCase testCase = testCaseWrapper.getTestCase();
        String path = testCase.getUrl();
        String parameters = testCase.getParameters();
        Execution.logs.add("【发起Web请求】请求的方式为：GET");
        Execution.logs.add("【发起Web请求】请求的路径为："+path);
        Execution.logs.add("【发起Web请求】请求的参数为："+parameters);
        if(isStringToMap(parameters)){
            Map<String,String> paramsMap = JacksonUtil.fromJson(parameters,Map.class);
            return given()
                    .spec(spec)
                    .params(paramsMap)
                    .when()
                    .get(path)
                    .then()
                    .extract()
                    .jsonPath();
        }else{
            if(parameters == null){
                parameters = "";
            }else{
                parameters = parameters;
            }
            return given()
                    .spec(spec)
                    .when()
                    .get(path + parameters)
                    .then()
                    .extract()
                    .jsonPath();
        }
    }

    /**
     * 发起http的put请求
     * @param testCaseWrapper
     * @param spec
     * @return
     */
    private JsonPath sendPutRequest(TestCaseWrapper testCaseWrapper, RequestSpecification spec){
        TestCase testCase = testCaseWrapper.getTestCase();
        String path = testCase.getUrl();
        String parameters = testCase.getParameters();
        Execution.logs.add("【发起Web请求】请求的方式为：PUT");
        Execution.logs.add("【发起Web请求】请求的路径为："+path);
        Execution.logs.add("【发起Web请求】请求的参数为："+parameters);
        return given()
                .spec(spec)
                .body(parameters)
                .when()
                .put(path)
                .then()
                .extract()
                .jsonPath();
    }

    /**
     * 发起http的POST请求
     * @param testCaseWrapper
     * @param spec
     * @return
     */
    private JsonPath sendPostRequest(TestCaseWrapper testCaseWrapper, RequestSpecification spec){
        TestCase testCase = testCaseWrapper.getTestCase();
        String path = testCase.getUrl();
        String parameters = testCase.getParameters();
        Execution.logs.add("【发起Web请求】请求的方式为：POST");
        Execution.logs.add("【发起Web请求】请求的路径为："+path);
        Execution.logs.add("【发起Web请求】请求的参数为："+parameters);
        return given()
                .spec(spec)
                .body(parameters)
                .when()
                .post(path)
                .then()
                .extract()
                .jsonPath();
    }

    /**
     * 发起http的DELETE请求
     * @param testCaseWrapper
     * @param spec
     * @return
     */
    private JsonPath sendDeleteRequest(TestCaseWrapper testCaseWrapper, RequestSpecification spec){
        TestCase testCase = testCaseWrapper.getTestCase();
        String path = testCase.getUrl();
        String parameters = testCase.getParameters();
        Execution.logs.add("【发起Web请求】请求的方式为：DELETE");
        Execution.logs.add("【发起Web请求】请求的路径为："+path);
        Execution.logs.add("【发起Web请求】请求的参数为："+parameters);
        if(isStringToMap(parameters)){
            Map<String,String> paramsMap = JacksonUtil.fromJson(parameters,Map.class);
            return given()
                    .spec(spec)
                    .params(paramsMap)
                    .when()
                    .delete(path)
                    .then()
                    .extract()
                    .jsonPath();
        }else {
            return given()
                    .spec(spec)
                    .when()
                    .delete(path+parameters)
                    .then()
                    .extract()
                    .jsonPath();
        }
    }

    /**
     * 生成一个RequestSpecification，用于请求的基本配置
     * @param testCaseWrapper
     * @return
     */
    private RequestSpecification initSpec(TestCaseWrapper testCaseWrapper){
        TestCase testCase = testCaseWrapper.getTestCase();
        String baseUri = testCaseWrapper.getUrlEnv();
        String headers = testCase.getHeaders();
        Map<String,String> headersMap = JacksonUtil.fromJson(headers, HashMap.class);
        Execution.logs.add("【发起Web请求】请求的服务器地址为："+baseUri);
        Execution.logs.add("【发起Web请求】请求的头部信息为："+headers);
        return new RequestSpecBuilder()
                .addHeaders(headersMap)
                .setBaseUri(baseUri)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .build();

    }

    private boolean isStringToMap(String source){
        try{
            JacksonUtil.fromJson(source,Map.class);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

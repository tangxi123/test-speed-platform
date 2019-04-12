package org.tangxi.testplatform.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class TestCase {

    private Long id;

    @NotBlank(message = "suite字段不能为空")
    @Size(min=1,max = 100,message = "suite字段个数应大于1小于100")
    private String suite;

    private String testModule;

    private List<String> groups;

    private String testName;

    private String descs;

    private String method;

    private String baseUrl;

    private String url;

    private String headers;

    private String parameters;

    private List<String> preActionNames;

    private List<CheckPoint> checkPoints;

    private List<String> postActionNames;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String actual;

    private int is_passed;

    public int getIs_passed() {
        return is_passed;
    }

    public void setIs_passed(int is_passed) {
        this.is_passed = is_passed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getTestModule() {
        return testModule;
    }

    public void setTestModule(String testModule) {
        this.testModule = testModule;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public List<String> getPreActionNames() {
        return preActionNames;
    }

    public void setPreActionNames(List<String> preActionNames) {
        this.preActionNames = preActionNames;
    }

    public List<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<CheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public List<String> getPostActionNames() {
        return postActionNames;
    }

    public void setPostActionNames(List<String> postActionNames) {
        this.postActionNames = postActionNames;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    @Override
    public String toString(){
        return JacksonUtil.toJson(this);
    }
}

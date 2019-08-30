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

    private String suite;

    private int moduleId;

    private int baseUrlId;

    private String testName;

    private String descs;

    private String method;

    private String url;

    private String headers;

    private String parameters;

    private List<String> preActionNames;

    private List<CheckPoint> checkPoints;

    private List<String> postActionNames;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private long executionTime;

    private int isPassed;

    private int isDeleted;

    private int isDsabled;

    private String result;

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public int getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(int isPassed) {
        this.isPassed = isPassed;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getIsDsabled() {
        return isDsabled;
    }

    public void setIsDsabled(int isDsabled) {
        this.isDsabled = isDsabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getBaseUrlId() {
        return baseUrlId;
    }

    public void setBaseUrlId(int baseUrlId) {
        this.baseUrlId = baseUrlId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString(){
        return JacksonUtil.toJson(this);
    }
}

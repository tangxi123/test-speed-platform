package org.tangxi.testplatform.model.log;

import java.util.List;

public class TestCaseLog {
    private int id;
    private int testCaseId;
    private String testCaseName;
    private String name;
    private List<String> logs;
    private int passedTcCount;
    private int failedTcCount;
    private int testResultStatus;
    private int moduleId;
    private String reportId;
    private String result;
    private long executionTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public int getPassedTcCount() {
        return passedTcCount;
    }

    public void setPassedTcCount(int passedTcCount) {
        this.passedTcCount = passedTcCount;
    }

    public int getFailedTcCount() {
        return failedTcCount;
    }

    public void setFailedTcCount(int failedTcCount) {
        this.failedTcCount = failedTcCount;
    }

    public int getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(int testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}

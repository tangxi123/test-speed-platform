package org.tangxi.testplatform.model.testReport;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;
import java.util.List;

public class Report {
    private String id;
    private String moduleId;
    private String reportName;
    private LocalDateTime reportTime;
    private int passedTcCount;
    private int failedTcCount;
    private int skippedTcCount;
    private int testResultStatus;
    private List<Integer> testCaseLogIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
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

    public int getSkippedTcCount() {
        return skippedTcCount;
    }

    public void setSkippedTcCount(int skippedTcCount) {
        this.skippedTcCount = skippedTcCount;
    }

    public int getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(int testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public List<Integer> getTestCaseLogIds() {
        return testCaseLogIds;
    }

    public void setTestCaseLogIds(List<Integer> testCaseLogIds) {
        this.testCaseLogIds = testCaseLogIds;
    }


}

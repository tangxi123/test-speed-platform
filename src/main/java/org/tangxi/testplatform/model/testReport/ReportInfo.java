package org.tangxi.testplatform.model.testReport;

import java.util.List;

public class ReportInfo {
    private String id;
    private String moduleName;
    private int moduleId;
    private String reportId;
    private int passedTcCount;
    private int failedTcCount;
    private int skippedTcCount;
    private long runtime;
    private int testResultStatus;
    private List<Integer> testCaseLogIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
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

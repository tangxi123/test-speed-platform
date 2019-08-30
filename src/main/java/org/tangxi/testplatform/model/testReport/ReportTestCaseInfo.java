package org.tangxi.testplatform.model.testReport;

import java.util.List;

public class ReportTestCaseInfo {
    private String id;
    private String reportInfoId;
    private String testCaseName;
    private int testResultStatus;
    private String exceptions;
    private List<String> logs;
    private long runtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportInfoId() {
        return reportInfoId;
    }

    public void setReportInfoId(String reportInfoId) {
        this.reportInfoId = reportInfoId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public int getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(int testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public String getExceptions() {
        return exceptions;
    }

    public void setExceptions(String exceptions) {
        this.exceptions = exceptions;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }


}

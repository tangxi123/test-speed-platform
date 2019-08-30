package org.tangxi.testplatform.model.log;

import java.util.List;

public class ModuleMapLog {
    private int id;
    private int moduleId;
    private String name;
    private String descs;
    private int parentModuleId;
    private int passedCount;
    private int failedCount;
    private int testResultStatus;
    private List<TestCaseLog> execTestCaseLog;
    private String reportId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getParentModuleId() {
        return parentModuleId;
    }

    public void setParentModuleId(int parentModuleId) {
        this.parentModuleId = parentModuleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }


    public int getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public int getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(int testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public List<TestCaseLog> getExecTestCaseLog() {
        return execTestCaseLog;
    }

    public void setExecTestCaseLog(List<TestCaseLog> execTestCaseLog) {
        this.execTestCaseLog = execTestCaseLog;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}

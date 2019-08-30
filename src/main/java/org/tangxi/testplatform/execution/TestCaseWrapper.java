package org.tangxi.testplatform.execution;

import com.jayway.restassured.path.json.JsonPath;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class TestCaseWrapper {
    private TestCase testCase;
    private String urlEnv;
    private List<PrePostActionWrapper> preActions;
    private List<PrePostActionWrapper> postActions;
    private LinkedHashMap<String, Object> selectPreParams = new LinkedHashMap<>();
    private List<String> checkResult;
    private Integer testCaseLogId;

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public String getUrlEnv() {
        return urlEnv;
    }

    public void setUrlEnv(String urlEnv) {
        this.urlEnv = urlEnv;
    }

    public List<PrePostActionWrapper> getPreActions() {
        return preActions;
    }

    public void setPreActions(List<PrePostActionWrapper> preActions) {
        this.preActions = preActions;
    }

    public List<PrePostActionWrapper> getPostActions() {
        return postActions;
    }

    public void setPostActions(List<PrePostActionWrapper> postActions) {
        this.postActions = postActions;
    }


    public LinkedHashMap<String, Object> getSelectPreParams() {
        return selectPreParams;
    }

    public void setSelectPreParams(LinkedHashMap<String, Object> selectPreParams) {
        this.selectPreParams.putAll(selectPreParams);
    }

    public void clearSelectPreParams(){
        this.selectPreParams.clear();
    }

    public List<String> getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(List<String> checkResult) {
        this.checkResult = checkResult;
    }

    public Integer getTestCaseLogId() {
        return testCaseLogId;
    }

    public void setTestCaseLogId(Integer testCaseLogId) {
        this.testCaseLogId = testCaseLogId;
    }
}

package org.tangxi.testplatform.execution.steps.replace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.execution.aop.ExecutionTime;
import org.tangxi.testplatform.execution.aop.LOG;
import org.tangxi.testplatform.mapper.DatabaseConfigMapper;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.mapper.PrePostActionMapper;
import org.tangxi.testplatform.mapper.UrlMapper;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.Url;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;
import org.tangxi.testplatform.model.prePostAction.PrePostActionType;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReplaceTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceTestCase.class);

    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示下划线数字和字母

    public static final List<String> logs = new ArrayList<>();

    @Autowired
    PrePostActionMapper actionMapper;

//    @Autowired
//    ReplaceTestCase replaceTestCase;

    @Autowired
    TestCaseWrapper testCaseWrapper;

    @Autowired
    ReplaceHolder replaceHolder;

    @Autowired
    UrlMapper urlMapper;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    ReplaceAction replaceAction;

    @Autowired
    ReplaceCheckPoint replaceCheckPoint;

    @Autowired
    DatabaseConfigMapper databaseConfigMapper;

    /**
     * 替换测试用例
     *
     * @param testCase
     * @return
     */
    public TestCaseWrapper generateReplacedTestCaseWrapper(TestCase testCase) {
        Execution.logs.add("开始替换关键字");
        String replacedTestName = replaceTestName(testCase.getTestName(), testCase.getModuleId()); //替换测试用例名字
        String replaceDescs = replaceDescs(testCase.getDescs(), testCase.getModuleId());//替换测试用例描述
        String replacedUrl = replaceUrl(testCase.getUrl(), testCase.getModuleId());//替换URL
        String replacedHeaders = replaceHeaders(testCase.getHeaders(), testCase.getModuleId());//替换headers
        String replacedParameters = replaceParameters(testCase.getParameters(), testCase.getModuleId());//替换parameters
        String replacedUrlEnv = replaceUrlEnv(testCase.getBaseUrlId());
        List<PrePostActionWrapper> replacedPreActions = replacePreActions(testCase.getPreActionNames(), testCase.getModuleId());//替换前置动作
        List<PrePostActionWrapper> replacedPostActions = replacePostActions(testCase.getPostActionNames(), testCase.getModuleId());//替换后置动作
        List<CheckPoint> replacedCheckPoints = replaceCheckPoints(testCase.getCheckPoints(), testCase.getModuleId());//替换检查点
        testCase.setTestName(replacedTestName); //设置测试用例的名字为替换参数过后的名字。
        testCase.setDescs(replaceDescs);
        testCase.setUrl(replacedUrl);
        testCase.setHeaders(replacedHeaders);
        testCase.setParameters(replacedParameters);
        testCase.setCheckPoints(replacedCheckPoints);
        testCaseWrapper.setTestCase(testCase);
        testCaseWrapper.setUrlEnv(replacedUrlEnv);
        testCaseWrapper.setPreActions(replacedPreActions);
        testCaseWrapper.setPostActions(replacedPostActions);
        return testCaseWrapper;
    }

    /**
     * 替换测试用例名字
     *
     * @param testName
     * @return
     */


    private String replaceTestName(String testName, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换测试用例名字");
        Execution.logs.add("【替换关键字】测试用例名字替换前为：" + testName);
        String replacedTestName = replaceHolder.replaceHolder(testName, PARAM_PLACEHOLDER, moduleId);
        Execution.logs.add("【替换关键字】测试用例名字替换后为：" + replacedTestName);
        return replacedTestName;
    }

    /**
     * 替换测试用例描述
     *
     * @param descs
     * @param moduleId
     * @return
     */
    private String replaceDescs(String descs, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换测试用例描述");
        Execution.logs.add("【替换关键字】测试用例描述替换前为：" + descs);
        String replaceDescs = replaceHolder.replaceHolder(descs, PARAM_PLACEHOLDER, moduleId);
        Execution.logs.add("【替换关键字】测试用例描述替换后为：" + replaceDescs);
        return replaceDescs;
    }

    /**
     * 替换url
     *
     * @param url
     * @return
     */
    private String replaceUrl(String url, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换请求路径");
        Execution.logs.add("【替换关键字】请求路径替换前为：" + url);
        String replacedUrl = replaceHolder.replaceHolder(url, PARAM_PLACEHOLDER, moduleId);
        Execution.logs.add("【替换关键字】请求路径替换后为：" + replacedUrl);
        return replacedUrl;
    }

    /**
     * 替换headers
     *
     * @param headers
     * @return
     */
    private String replaceHeaders(String headers, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换请求头");
        Execution.logs.add("【替换关键字】请求头替换前为：" + headers);
        String replacedHeaders = replaceHolder.replaceHolder(headers, PARAM_PLACEHOLDER, moduleId);
        Execution.logs.add("【替换关键字】请求头替换后为：" + replacedHeaders);
        return replacedHeaders;
    }


    /**
     * 替换参数
     *
     * @param parameters
     * @return
     */
    private String replaceParameters(String parameters, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换参数");
        Execution.logs.add("【替换关键字】参数替换前为：" + parameters);
        String replacedParameters = replaceHolder.replaceHolder(parameters, PARAM_PLACEHOLDER, moduleId);
        Execution.logs.add("【替换关键字】参数替换后为：" + replacedParameters);
        return replacedParameters;
    }

    /**
     * 替换基础url环境地址
     *
     * @param baseUrlId
     * @return
     */
    private String replaceUrlEnv(int baseUrlId) {
        Url urlById = urlMapper.getUrlById(baseUrlId);
        return urlById.getUrl();
    }

    /**
     * 替换所有前置动作的参数
     *
     * @param actionIds
     * @param moduleId
     * @return
     */
    private List<PrePostActionWrapper> replacePreActions(List<String> actionIds, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换前置动作");
        return replaceAction.replaceActions(actionIds, moduleId);
    }

    /**
     * 替换所有后置动作的参数
     *
     * @param actionIds
     * @param moduleId
     * @return
     */
    private List<PrePostActionWrapper> replacePostActions(List<String> actionIds, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换后置动作");
        return replaceAction.replaceActions(actionIds, moduleId);
    }

    private List<CheckPoint> replaceCheckPoints(List<CheckPoint> checkPoints, int moduleId) {
        Execution.logs.add("【替换关键字】开始替换检查点");
        return replaceCheckPoint.replaceCheckPoints(checkPoints, moduleId);
    }

    private List<PrePostActionWrapper> getActions(List<String> actionIds) {
        List<PrePostActionWrapper> replacedActions = new ArrayList<>();
        for (String actionId : actionIds) {
            PrePostActionWrapper actionWrapper = actionMapper.getActionById(Integer.parseInt(actionId));
            if (actionWrapper == null) {
                throw new TestCaseRunException("没有可执行的前置动作，前置动作id为：" + actionId);
            }
            PrePostActionType actionType = actionWrapper.getActionType();
            switch (actionType) {
                case SQL:
                    PrePostActionWrapper sqlActionWrapper = actionMapper.getSqlActionById(actionWrapper.getId());
                    replacedActions.add(sqlActionWrapper);
                    break;
                default:
                    throw new TestCaseRunException("不支持的动作类型：" + actionType);
            }
        }
        return replacedActions;
    }

}

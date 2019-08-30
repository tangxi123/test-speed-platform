package org.tangxi.testplatform.execution.steps.requests;

import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.execution.steps.actions.ReplaceActionParam;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import java.util.List;

@Component
public class ReplaceSelectParam {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceSelectParam.class);

    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示任意字符

    @Autowired
    ReplaceActionHolder replaceActionHolder;

    @Autowired
    ReplaceActionParam replaceActionParam;

    @Autowired
    ReplaceCheckPointActionHolder replaceCheckPointActionHolder;

    @Autowired
    ReplaceActionsActionHolder replaceActionsActionHolder;
    /**
     * 将测试用例里的前后置动作参数，替换为实际值
     * @param testCaseWrapper
     * @return
     */
    public TestCaseWrapper replaceTestCaseWrapper(TestCaseWrapper testCaseWrapper,String prePost){
        Execution.logs.add("【替换前后置参数】开始替换测试用例里的前后置参数");
        TestCase testCase = testCaseWrapper.getTestCase();
        String replacedTestName = replaceTestName(testCase.getTestName(),prePost);
        String replaceDescs = replaceDescs(testCase.getDescs(),prePost);
        String replacedUrl = replaceUrl(testCase.getUrl(),prePost);
        String replacedHeaders = replaceHeaders(testCase.getHeaders(),prePost);
        String replacedParameters = replaceParameters(testCase.getParameters(),prePost);
        List<CheckPoint> replacedCheckPoints = replaceCheckPoints(testCase.getCheckPoints(),prePost);
        List<PrePostActionWrapper> replacedPostActions = replacePostActions(testCaseWrapper.getPostActions(),prePost);
        testCase.setTestName(replacedTestName);
        testCase.setDescs(replaceDescs);
        testCase.setUrl(replacedUrl);
        testCase.setHeaders(replacedHeaders);
        testCase.setParameters(replacedParameters);
        testCase.setCheckPoints(replacedCheckPoints);
//        List<PrePostActionWrapper> replacedPreActions = replacePreActions(testCaseWrapper.getPreActions());  前置动作已经执行完了，不需要再去替换前置动作里的前置参数。
        testCaseWrapper.setTestCase(testCase);
        testCaseWrapper.setPostActions(replacedPostActions);
        return testCaseWrapper;
    }

    /**
     * 替换测试用例名字
     * @param testName
     * @return
     */
    private String replaceTestName(String testName,String prePost){
        Execution.logs.add("【替换前后置参数】开始替换测试用例名字");
        Execution.logs.add("【替换前后置参数】测试用例名字替换前为："+testName);
        String replacedTestName = replaceActionHolder.replaceHolder(testName, prePost, PARAM_PLACEHOLDER);
        Execution.logs.add("【替换前后置参数】测试用例名字替换后为："+replacedTestName);
        return replacedTestName;
    }

    /**
     * 替换测试用例描述
     * @param descs
     * @return
     */
    private String replaceDescs(String descs,String prePost){
        Execution.logs.add("【替换前后置参数】开始替换测试用例描述");
        Execution.logs.add("【替换前后置参数】测试用例描述替换前为："+descs);
        String replacedDescs = replaceActionHolder.replaceHolder(descs, prePost, PARAM_PLACEHOLDER);
        Execution.logs.add("【替换前后置参数】测试用例描述替换后为："+replacedDescs);
        return replacedDescs;
    }

    /**
     * 替换测试用例url
     * @param url
     * @return
     */
    private String replaceUrl(String url,String prePost){
        Execution.logs.add("【替换前后置参数】开始替换UrL路径");
        Execution.logs.add("【替换前后置参数】UrL路径替换前为："+url);
        String replacedUrl = replaceActionHolder.replaceHolder(url, prePost, PARAM_PLACEHOLDER);
        Execution.logs.add("【替换前后置参数】UrL路径替换后为："+replacedUrl);
        return replacedUrl;
    }

    /**
     * 替换测试用例头部数据的前置参数
     * @param headers
     * @return
     */
    private String replaceHeaders(String headers,String prePost){
        Execution.logs.add("【替换前后置参数】开始替换头部");
        Execution.logs.add("【替换前后置参数】头部替换前为："+headers);
        String replacedHeader = replaceActionHolder.replaceHolder(headers, prePost, PARAM_PLACEHOLDER);
        Execution.logs.add("【替换前后置参数】头部替换后为："+replacedHeader);
        return replacedHeader;
    }

    /**
     * 替换测试用例里的参数的前置参数
     * @param parameters
     * @return
     */
    private String replaceParameters(String parameters,String prePost){
        Execution.logs.add("【替换前后置参数】开始替换请求参数");
        Execution.logs.add("【替换前后置参数】请求参数替换前为："+parameters);
        String replacedParam = replaceActionHolder.replaceHolder(parameters, prePost, PARAM_PLACEHOLDER);
        Execution.logs.add("【替换前后置参数】请求参数替换后为："+replacedParam);
        return replacedParam;
    }

    /**
     * 替换测试用例里的检查点的前置参数
     * @param checkPoints
     * @return
     */
    private List<CheckPoint> replaceCheckPoints(List<CheckPoint> checkPoints,String prePost){
        return replaceCheckPointActionHolder.replaceCheckPoints(checkPoints,prePost);
    }

    private List<PrePostActionWrapper> replacePostActions(List<PrePostActionWrapper> postActionWrappers,String prePost){
        return replaceActionsActionHolder.replaceActions(postActionWrappers,prePost);
    }
}

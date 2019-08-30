package org.tangxi.testplatform.execution.steps.requests;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.execution.TestCaseWrapper;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReplaceActionHolder {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceActionHolder.class);

    private static final String PRE = "pre.";
    private static final String POST = "post.";

    @Autowired
    TestCaseWrapper testCaseWrapper;

    /**
     * 替换占位符
     *
     * @param source
     * @param regex
     * @return
     */
    public String replaceHolder(String source, String prePost, String regex) {
        LOG.info("需要替换的语句为：" + source + "替换的正则表达式为：" + regex);
        if (StringUtils.isBlank(source) || regex == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String match = matcher.group();
            String parseFields = replaceMatch(match, prePost);
            source = source.replace(match, parseFields);
        }
        LOG.info("替换后的字符串为：" + source);
        return source;
    }

    private String replaceMatch(String match, String prePost) {
        String regex = match.substring(match.indexOf("{") + 1, match.indexOf("}"));
        Object selectResult;
        LOG.info("需要替换的参数为：" + regex);
        if (regex.startsWith(prePost)) {
            LOG.info("regex为：" + regex);
            LinkedHashMap<String, Object> selectPreParams = null;
            selectPreParams = testCaseWrapper.getSelectPreParams();
            for (String key : selectPreParams.keySet()) {
                if (key.equals(match)) {
                    selectResult = selectPreParams.get(match);
                    return "" + selectResult;
                }
            }
            return regex;
        }
        return match;
    }
}

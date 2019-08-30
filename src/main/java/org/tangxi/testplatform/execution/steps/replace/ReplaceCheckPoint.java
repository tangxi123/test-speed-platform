package org.tangxi.testplatform.execution.steps.replace;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplaceCheckPoint {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceCheckPoint.class);

    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示下划线数字和字母

    public static final List<String> logs = new ArrayList<>();

    @Autowired
    ReplaceHolder replaceHolder;


    /**
     * 替换检查点里面的参数占位符
     * @param checkPoints
     * @param moduleId
     * @return
     */
    public List<CheckPoint> replaceCheckPoints(List<CheckPoint> checkPoints, int moduleId){
        List<CheckPoint> replacedCheckPoints = new ArrayList<>();
        String scheckPointsStr = JacksonUtil.toJson(checkPoints);
        Execution.logs.add("【替换关键字】检查点替换前为："+scheckPointsStr);
        List<CheckPoint> checkPointList = JacksonUtil.fromJson(scheckPointsStr, new TypeReference<List<CheckPoint>>() {
        });
        for(CheckPoint checkPoint : checkPointList){
            checkPoint = JacksonUtil.fromJson(JacksonUtil.toJson(checkPoint), new TypeReference<CheckPoint>() {
            });
            String replacedCheckKey = replaceCheckKey(checkPoint.getCheckKey(),moduleId);
            String replacedExpected = replaceExpected(checkPoint.getExpected(),moduleId);
            checkPoint.setCheckKey(replacedCheckKey);
            checkPoint.setExpected(replacedExpected);
            replacedCheckPoints.add(checkPoint);
        }
        Execution.logs.add("【替换关键字】检查点替换后为："+JacksonUtil.toJson(replacedCheckPoints));
        return replacedCheckPoints;
    }

    /**
     * 替换检查点里的checkKey
     * @param checkKey
     * @param moduleId
     * @return
     */
    private String replaceCheckKey(String checkKey, int moduleId){
        return replaceHolder.replaceHolder(checkKey,PARAM_PLACEHOLDER,moduleId);
    }

    /**
     * 替换检查点里的expected
     * @param expected
     * @param moduleId
     * @return
     */
    private String replaceExpected(String expected, int moduleId){
        return replaceHolder.replaceHolder(expected,PARAM_PLACEHOLDER,moduleId);
    }
}


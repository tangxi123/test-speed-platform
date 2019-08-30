package org.tangxi.testplatform.execution.steps.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.checkPoint.CheckPoint;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplaceCheckPointActionHolder {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceCheckPointActionHolder.class);

    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示任意字符

    @Autowired
    ReplaceActionHolder replaceActionHolder;

    /**
     * 替换checkPoints里的动作参数
     * @param checkPoints
     * @return
     */
    public List<CheckPoint> replaceCheckPoints(List<CheckPoint> checkPoints,String prePost){
        List<CheckPoint> replacedCheckPoints = new ArrayList<>();
        String scheckPointsStr = JacksonUtil.toJson(checkPoints);
        List<CheckPoint> checkPointList = JacksonUtil.fromJson(scheckPointsStr, new TypeReference<List<CheckPoint>>() {
        });
        for(CheckPoint checkPoint : checkPointList){
            checkPoint = JacksonUtil.fromJson(JacksonUtil.toJson(checkPoint), new TypeReference<CheckPoint>() {
            });
            String replacedCheckKey = replaceCheckKey(checkPoint.getCheckKey(),prePost);
            String replacedExpected = replaceExpected(checkPoint.getExpected(),prePost);
            checkPoint.setCheckKey(replacedCheckKey);
            checkPoint.setExpected(replacedExpected);
            replacedCheckPoints.add(checkPoint);
        }
        return replacedCheckPoints;
    }

    /**
     * 替换检查点里的checkKey
     * @param checkKey
     * @return
     */
    private String replaceCheckKey(String checkKey,String prePost){
        return replaceActionHolder.replaceHolder(checkKey,prePost,PARAM_PLACEHOLDER);
    }

    /**
     * 替换检查点里的expected
     * @param expected
     * @return
     */
    private String replaceExpected(String expected,String prePost){
        return replaceActionHolder.replaceHolder(expected,prePost,PARAM_PLACEHOLDER);
    }


}

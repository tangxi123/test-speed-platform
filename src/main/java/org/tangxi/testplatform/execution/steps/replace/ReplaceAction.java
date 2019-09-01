package org.tangxi.testplatform.execution.steps.replace;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.mapper.PrePostActionMapper;
import org.tangxi.testplatform.model.prePostAction.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplaceAction {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceAction.class);
    public static final List<String> logs = new ArrayList<>();

    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示下划线数字和字母

    @Autowired
    ReplaceHolder replaceHolder;

    @Autowired
    PrePostActionMapper actionMapper;

    /**
     * 替换动作配置里的参数
     * @param actionIds
     * @param moduleId
     * @return
     */
    public List<PrePostActionWrapper> replaceActions(List<String> actionIds, int moduleId){
        List<PrePostActionWrapper> replacedActions = new ArrayList<>();
        if(actionIds.size() == 0) {
            Execution.logs.add("【替换关键字】没有动作");
        }else{
            for(String actionId : actionIds){
                PrePostActionWrapper actionWrapper = actionMapper.getActionById(Integer.parseInt(actionId));
                if(actionWrapper == null){
                    throw new TestCaseRunException("没有可执行的前置动作，前置动作id为："+actionId);
                }
                PrePostActionType actionType = actionWrapper.getActionType();
                switch (actionType){
                    case SQL:
                        PrePostActionWrapper sqlActionWrapper = actionMapper.getSqlActionById(actionWrapper.getId());
                        Execution.logs.add("【替换关键字】动作替换前为："+JacksonUtil.toJson(sqlActionWrapper));
                        PrePostActionWrapper replaceActionSQLWrapper = replaceSQLTypeAction(actionWrapper, moduleId);
                        Execution.logs.add("【替换关键字】动作替换后为："+JacksonUtil.toJson(replaceActionSQLWrapper));
                        replacedActions.add(replaceActionSQLWrapper);
                        break;
                    default:
                        throw new TestCaseRunException("不支持的动作类型："+actionType);
                }
            }
        }

        return replacedActions;
    }
    /**
     * 替换sql类型的动作参数占位符
     * @param actionWrapper
     * @param moduleId
     * @return
     */
    private PrePostActionWrapper replaceSQLTypeAction(PrePostActionWrapper actionWrapper, int moduleId){
        String replacedActionName = replaceActionName(actionWrapper.getName(),moduleId);
        String replacedActionDescs = replaceActionDescs(actionWrapper.getDescs(),moduleId);
        actionWrapper.setName(replacedActionName);
        actionWrapper.setDescs(replacedActionDescs);
        PrePostActionWrapper sqlActionWrapper = actionMapper.getSqlActionById(actionWrapper.getId());
        List<SqlInfo> replacedSql = replaceActionSql(sqlActionWrapper.getAction().getSql(),moduleId);
        PrePostActionSql replacedAction = (PrePostActionSql)sqlActionWrapper.getAction();
        replacedAction.setSql(replacedSql);
        actionWrapper.setAction(replacedAction);
        return actionWrapper;
    }

    /**
     * 替换动作名称占位符
     * @param actionName
     * @param moduleId
     * @return
     */
    private String replaceActionName(String actionName,int moduleId){
        return replaceHolder.replaceHolder(actionName,PARAM_PLACEHOLDER,moduleId);
    }

    /**
     * 替换动作描述占位符
     * @param actionDescs
     * @param moduleId
     * @return
     */
    private String replaceActionDescs(String actionDescs,int moduleId){
        return replaceHolder.replaceHolder(actionDescs,PARAM_PLACEHOLDER,moduleId);
    }

    /**
     * 替换动作sql
     * @param sql
     * @param moduleId
     * @return
     */
    private List<SqlInfo> replaceActionSql(List<SqlInfo> sql, int moduleId){
        List<SqlInfo> replacedSqls = new ArrayList<>();
        String sqlStr = JacksonUtil.toJson(sql);
        List<SqlInfo> sqlInfos = JacksonUtil.fromJson(sqlStr, new TypeReference<List<SqlInfo>>() {
        });
        for(SqlInfo s : sqlInfos) {
            String replaceSql = replaceHolder.replaceHolder(s.getSql(), PARAM_PLACEHOLDER, moduleId);
            s.setSql(replaceSql);
            replacedSqls.add(s);
        }
        return replacedSqls;
    }
}

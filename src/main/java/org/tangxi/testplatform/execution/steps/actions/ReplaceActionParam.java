package org.tangxi.testplatform.execution.steps.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.execution.steps.requests.ReplaceActionHolder;
import org.tangxi.testplatform.model.prePostAction.PrePostActionSql;
import org.tangxi.testplatform.model.prePostAction.PrePostActionType;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;
import org.tangxi.testplatform.model.prePostAction.SqlInfo;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplaceActionParam {
    private static final Logger LOG = LoggerFactory.getLogger(ReplaceActionParam.class);

    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示任意字符

    @Autowired
    ReplaceActionHolder replaceActionHolder;

    /**
     * 替换前置动作里的动作参数
     * @param actionWrappers
     * @return
     */
    public List<PrePostActionWrapper> replaceActions(List<PrePostActionWrapper> actionWrappers,String prePost){
        List<PrePostActionWrapper> replacedActions = new ArrayList<>();
        for(PrePostActionWrapper actionWrapper : actionWrappers){
            PrePostActionType actionType = actionWrapper.getActionType();
            switch (actionType){
                case SQL:
                    PrePostActionWrapper replaceActionSQLWrapper = replaceSQLTypeAction(actionWrapper,prePost);
                    replacedActions.add(replaceActionSQLWrapper);
                    break;
            }
        }
        return replacedActions;
    }

    /**
     * 替换动作里类型为sql的动作参数
     * @param actionWrapper
     * @return
     */
    private PrePostActionWrapper replaceSQLTypeAction(PrePostActionWrapper actionWrapper,String prePost){
        String replacedActionName = replaceActionName(actionWrapper.getName(),prePost);
        String replacedActionDescs = replaceActionDescs(actionWrapper.getDescs(),prePost);
        actionWrapper.setName(replacedActionName);
        actionWrapper.setDescs(replacedActionDescs);
        PrePostActionSql sqlAction = (PrePostActionSql)actionWrapper.getAction();
        List<SqlInfo> replacedSql = replaceSql(sqlAction.getSql(),prePost);
        sqlAction.setSql(replacedSql);
        actionWrapper.setAction(sqlAction);
        return actionWrapper;
    }

    /**
     * 替换动作名称的动作参数占位符
     * @param actionName
     * @return
     */
    private String replaceActionName(String actionName,String prePost){
        return replaceActionHolder.replaceHolder(actionName,prePost,PARAM_PLACEHOLDER);
    }

    /**
     * 替换动作描述里的动作参数占位符
     * @param descs
     * @return
     */
    private String replaceActionDescs(String descs,String prePost){
        return replaceActionHolder.replaceHolder(descs,prePost,PARAM_PLACEHOLDER);
    }

    /**
     * 替换sql语句里的动作参数占位符
     * @param sql
     * @return
     */
    private List<SqlInfo> replaceSql(List<SqlInfo> sql,String prePost){
        List<SqlInfo> replacedSqls = new ArrayList<>();
        for(SqlInfo s : sql){
            String replaceSql = replaceActionHolder.replaceHolder(s.getSql(), prePost, PARAM_PLACEHOLDER);
            s.setSql(replaceSql);
            replacedSqls.add(s);
        }
        return replacedSqls;
    }
}

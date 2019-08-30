package org.tangxi.testplatform.execution.steps.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.model.prePostAction.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplaceActionsActionHolder {
    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示任意字符

    @Autowired
    ReplaceActionHolder replaceActionHolder;

    /**
     * 替换动作里的动作参数
     * @param actionWrappers
     * @return
     */
    public List<PrePostActionWrapper> replaceActions(List<PrePostActionWrapper> actionWrappers,String prePost){
        List<PrePostActionWrapper> replacedActions = new ArrayList<>();
        for(PrePostActionWrapper actionWrapper : actionWrappers){
            PrePostActionType actionType = actionWrapper.getActionType();
            switch (actionType){
                case SQL:
                    PrePostActionWrapper replacedActionSqlWrapper = replaceSqlTypeAction(actionWrapper,prePost);
                    replacedActions.add(replacedActionSqlWrapper);
                    break;
            }
        }
        return replacedActions;
    }

    /**
     * 替换sql类型动作里的动作参数
     * @param actionWrapper
     * @return
     */
    private PrePostActionWrapper replaceSqlTypeAction(PrePostActionWrapper actionWrapper,String prePost){
        String replacedActionName = replaceActionName(actionWrapper.getName(),prePost);
        String replacedActionDescs = replaceActionDescs(actionWrapper.getDescs(),prePost);
        actionWrapper.setName(replacedActionName);
        actionWrapper.setDescs(replacedActionDescs);
        PrePostActionSql action = (PrePostActionSql)actionWrapper.getAction();
        List<SqlInfo> replacedSql = replaceActionSql(action.getSql(),prePost);
        action.setSql(replacedSql);
        actionWrapper.setAction(action);
        return actionWrapper;
    }

    /**
     * 替换动作名称里的动作参数
     * @param actionName
     * @return
     */
    private String replaceActionName(String actionName,String prePost){
        return replaceActionHolder.replaceHolder(actionName,prePost,PARAM_PLACEHOLDER);
    }

    /**
     * 替换动作描述里的动作参数
     * @param actionDescs
     * @return
     */
    private String replaceActionDescs(String actionDescs,String prePost){
        return replaceActionHolder.replaceHolder(actionDescs,prePost,PARAM_PLACEHOLDER);
    }

    private List<SqlInfo> replaceActionSql(List<SqlInfo> sql,String prePost){
        List<SqlInfo> replacedSqls = new ArrayList<>();
        for(SqlInfo s : sql) {
            String replaceSql = replaceActionHolder.replaceHolder(s.getSql(), prePost, PARAM_PLACEHOLDER);
            s.setSql(replaceSql);
            replacedSqls.add(s);
        }
        return replacedSqls;
    }

}

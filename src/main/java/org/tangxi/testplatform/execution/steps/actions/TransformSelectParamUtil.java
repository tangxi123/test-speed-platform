package org.tangxi.testplatform.execution.steps.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.steps.replace.ReplaceCheckPoint;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransformSelectParamUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TransformSelectParamUtil.class);
    private static final String SELECT = "SELECT";
    private static final String select = "select";
    private static final String PRE = "pre";
    private static final String POST = "post";

    public static LinkedHashMap<String, Object> transformSelectPreParam(String sql,String prePost,LinkedHashMap<String, Object> selectResult,int index){
        return transformSelectParam(sql,prePost,selectResult,index);

    }

    private static LinkedHashMap<String, Object> transformSelectParam(String sql,String prePost,LinkedHashMap<String, Object> selectResult,int index){
        LinkedHashMap<String, Object> transformedSelectParam = new LinkedHashMap<>();
        String[] param = null;
        sql = sql.trim();
        if(sql.startsWith(SELECT) || sql.startsWith(select)){
            int selectIndex = sql.indexOf(SELECT)+SELECT.length();
            int fromIndex = sql.indexOf("FROM");
            String substring = sql.substring(selectIndex, fromIndex).trim();
            param = substring.split(",");
            LOG.info("select的字段为："+ JacksonUtil.toJson(param));

        }
        if(param == null){
            throw new TestCaseRunException("查询到的字段为空："+param);
        }
        for(int paramIndex = 0; paramIndex < param.length; paramIndex++){
            if(param[paramIndex].equals("*")){
                throw new TestCaseRunException("sql语句不支持查询*字段："+sql);
            }
        }
        int i = 0;
        for(Object value : selectResult.values()){
            LOG.info("key值为："+"${"+prePost+param[i]+"["+index+"]"+"}"+" "+"value值为："+value);  //将参数格式设置为：${pre.ztc.id[0]}或者${post.ztc.id[0]}
            String key = "${"+prePost+param[i]+"["+index+"]"+"}";
            transformedSelectParam.put(key,value);
            i++;
        }
        return transformedSelectParam;
    }


}

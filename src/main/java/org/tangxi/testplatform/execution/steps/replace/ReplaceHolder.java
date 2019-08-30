package org.tangxi.testplatform.execution.steps.replace;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.execution.aop.ModuleCheck;
import org.tangxi.testplatform.mapper.ModuleMapper;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.model.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReplaceHolder {
    private final static Logger LOG = LoggerFactory.getLogger(ReplaceHolder.class);
    public static final List<String> logs = new ArrayList<>();

    @Autowired
    ReplaceHolder replaceHolder;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    ModuleMapper moduleMapper;

    @Autowired
    ParamReplacement paramReplacement;


    public String replaceHolder(String source, String regex,int moduleId){
        if (StringUtils.isBlank(source) || regex == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()){
            String match = matcher.group();
            String parseFields = replaceParamMatch(match,moduleId);
            source = source.replace(match,parseFields);
        }
        LOG.info("替换后的字符串为："+source);
        return source;
    }

//    @ModuleCheck
//    public String replaceMatch(String match,int moduleId){
//        String regex = match.substring(match.indexOf("{") + 1, match.indexOf("}"));
//        return paramReplacement.execParamConfig(regex);//查找是否有在相同模块下的参数，如果有进行替换。
//
////        return match;
//    }

    public String replaceParamMatch(String match,int moduleId){
        String regex = match.substring(match.indexOf("{") + 1, match.indexOf("}"));
        LOG.info("需要替换的参数为："+regex);
        if(!regex.startsWith("pre.")) {  //如果不是前置参数，才执行这个替换操作
            //判断测试用例所属模块id是否跟参数所在同一个项目
                Integer paramId = parameterMapper.getParamIdByName(regex);
                if(paramId == null){
                    LOG.info("该模块下没有对应的参数设置"+regex);
                    return match;
                }
                int paramModuleId = parameterMapper.getParamWrapperById(paramId).getModuleId();
                List<Module> subModules = moduleMapper.getSubModulesById(paramModuleId);
                List<Integer> moduleIds = new ArrayList<>();
                for (Module module : subModules) {
                    moduleIds.add(module.getId());
                }
                if (moduleIds.contains(moduleId)) {
                    return paramReplacement.execParamConfig(regex);//查找是否有在相同模块下的参数，如果有进行替换。
                }
                return match; //如果没有在相同模块下的参数，直接返回参数key
        }
        return match;
    }
}

package org.tangxi.testplatform.execution.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.steps.replace.ReplaceHolder;
import org.tangxi.testplatform.execution.steps.replace.ReplaceTestCase;
import org.tangxi.testplatform.mapper.ModuleMapper;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.model.Module;
import org.tangxi.testplatform.model.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class ModuleCheckAspect {
    private final static Logger LOG = LoggerFactory.getLogger(ReplaceHolder.class);

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    ModuleMapper moduleMapper;


    @Around("@annotation(ModuleCheck)")
    public Object checkParamModule(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("*********************************************");
        Object[] args = joinPoint.getArgs();
        String match = (String) args[0];
        int moduleId = (int) args[1];
        boolean moduleMatch = checkModule(match,moduleId);
        if(moduleMatch){
            Object o = joinPoint.proceed();
            return o;
        }
        return match;
    }

    private boolean checkModule(String match,int moduleId){
        String regex = match.substring(match.indexOf("{") + 1, match.indexOf("}"));
        LOG.info("需要替换的参数为："+regex);
        if(!regex.startsWith("pre.")) {
            Integer paramId = parameterMapper.getParamIdByName(regex);
            if(paramId == null){
                LOG.info("该模块下没有对应的参数设置"+regex);
                return false;
            }
            int paramModuleId = parameterMapper.getParamWrapperById(paramId).getModuleId();
            List<Module> subModules = moduleMapper.getSubModulesById(paramModuleId);
            List<Integer> moduleIds = new ArrayList<>();
            for (Module module : subModules) {
                moduleIds.add(module.getId());
            }
            if (moduleIds.contains(moduleId)) {
                return true;//查找是否有在相同模块下的参数，如果有返回true
            }
        }
        return true;
    }


}

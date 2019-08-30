package org.tangxi.testplatform.model.parameter;

import org.apache.tomcat.jni.Local;
import org.tangxi.testplatform.common.util.JacksonUtil;

import java.time.LocalDateTime;

/**
 * 参数类型的包装类
 */
public class ParameterWrapper {
    private int id;

    private String name;

    private String descs;

    private ParameterType type; //参数类型，比如：sql类型、key-value类型、获取token类型

    private Parameter parameter; //具体的参数字段

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private int moduleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString(){
        return JacksonUtil.toJson(this);
    }
}

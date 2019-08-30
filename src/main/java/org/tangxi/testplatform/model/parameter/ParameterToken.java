package org.tangxi.testplatform.model.parameter;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Token类型的parameter
 */
@JsonTypeName("ParameterToken")
public class ParameterToken implements Parameter{
    private String id;
    private String url;
    private String headers;
    private String userData; //提交的用户名和密码以json格式存储
    private String token;
    private int paramId;

    public String getId() {
        return id;
    }

    @Override
    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getParamId() {
        return paramId;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }
}

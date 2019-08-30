package org.tangxi.testplatform.execution.steps.replace;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.specification.RequestSpecification;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.common.util.SqlSessionBuilder;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.mapper.DatabaseConfigMapper;
import org.tangxi.testplatform.mapper.ExecuteSqlMapper;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;
import org.tangxi.testplatform.model.parameter.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

@Component
public class ParamReplacement {
    private static final Logger LOG = LoggerFactory.getLogger(ParamReplacement.class);

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    DatabaseConfigMapper databaseConfigMapper;

    /**
     * 根据参数名获取参数配置，根据参数配置执行sql或者token等操作替换参数
     *
     * @param paramName
     * @return
     */
    public String execParamConfig(String paramName) {
        int paramId = parameterMapper.getParamIdByName(paramName);  //根据参数名获取参数id
        ParameterWrapper paramWrapper = parameterMapper.getParamWrapperById(paramId); //根据参数id获取详细的参数配置
        LOG.info("查询到的参数设置为：" + JacksonUtil.toJson(paramWrapper));
        return execParamByType(paramWrapper);   //根据不同类型执行参数操作
    }

    /**
     * 根据不同的参数类型执行不同的操作
     *
     * @param parameterWrapper
     * @return
     */
    private String execParamByType(ParameterWrapper parameterWrapper) {
        ParameterType type = parameterWrapper.getType();//获取参数类型
        switch (type) {
            case SQL:
                return execParamBySQLType(parameterWrapper);
            case TOKEN:
                return execParamByTokenType(parameterWrapper);
            case KEYVALUE:
                return execParamByKeyValueType(parameterWrapper);
            default:
                throw new UnexpectedTestCaseException("不存在类型为SQL、TOKEN、TOKEN的参数" + JacksonUtil.toJson(parameterWrapper));
        }
    }

    /**
     * 执行SQL类型的参数操作
     *
     * @param parameterWrapper
     * @return
     */
    private String execParamBySQLType(ParameterWrapper parameterWrapper) {
        int paramId = parameterWrapper.getId();
        ParameterWrapper sqlParameterWrapper = parameterMapper.getSqlParamById(paramId);
        ParameterSql parameterSql = (ParameterSql) sqlParameterWrapper.getParameter(); //获取sql类型的参数配置
        return dbConExec(parameterSql);
    }

    /**
     * 执行TOKEN类型的参数
     * @param parameterWrapper
     * @return
     */
    private String execParamByTokenType(ParameterWrapper parameterWrapper){
        Execution.logs.add("【替换关键字】开始执行Token类型的替换");
        int paramId = parameterWrapper.getId();
        ParameterWrapper tokenParameterWrapper = parameterMapper.getTokenParamById(paramId);
        ParameterToken parameterToken = (ParameterToken)tokenParameterWrapper.getParameter();
        JsonPath resultData = getToken(parameterToken);
        Execution.logs.add("【替换关键字】发起请求后，获取到的结果为："+resultData.prettyPrint());
        String accessToken = resultData.getString("accessToken");
        String tokenType = resultData.getString("tokenType");
        String token = accessToken + "\t" + tokenType;
        Execution.logs.add("【替换关键字】获取到的Autorization为："+token);
        return token;
    }


    /**
     * 执行keyValue类型的参数操作
     * @param parameterWrapper
     * @return
     */
    private String execParamByKeyValueType(ParameterWrapper parameterWrapper) {
        int paramId = parameterWrapper.getId();
        ParameterWrapper keyValueParamWrapper = parameterMapper.getKeyValueParamById(paramId);
        ParameterKeyValue parameterKeyValue = (ParameterKeyValue) keyValueParamWrapper.getParameter();
        return parameterKeyValue.getValue();
    }

    /**
     * 根据数据库配置连接数据源，并执行sql,因为这里是参数替换，所以暂时只支持select语句（后期优化）,默认查询得到第一个字段的值
     *
     * @param parameterSql
     * @return
     */
    private String dbConExec(ParameterSql parameterSql) {
        String result;
        int dbConfigId = parameterSql.getDbConfigId(); //获取参数的数据库配置id
        DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById("" + dbConfigId);//获取数据库配置
        SqlSession sqlSession = SqlSessionBuilder.generateSqlSession(databaseConfig);//根据数据库配置，建立一个mybatis的sql会话连接
        ExecuteSqlMapper executeSqlMapper = sqlSession.getMapper(ExecuteSqlMapper.class);//获取执行sql的mapper
        List<LinkedHashMap<String, Object>> select = null;//查询到返回的结果
        try {
            select = executeSqlMapper.select(parameterSql.getSql());
        } catch (Exception e) {
            LOG.error(e.toString());
            throw new TestCaseRunException("错误的sql语句:"+parameterSql.getSql());
        }
        result = (String) select.get(0).values().toArray()[0];//获取查询到的第一个字段的值
        LOG.info("执行sql语句后得到的结果为：" + JacksonUtil.toJson(select));
        LOG.info("查询得到的第一个字段的值为：" + result);
        return result;
    }

    /**
     * 获取token
     * @param parameterToken
     * @return
     */
    private JsonPath getToken(ParameterToken parameterToken){
        String url = parameterToken.getUrl();
        String headers = parameterToken.getHeaders();
        String userData = parameterToken.getUserData();
        Execution.logs.add("【替换关键字】获取Token的url为："+url);
        Execution.logs.add("【替换关键字】获取Token的headers为："+headers);
        Execution.logs.add("【替换关键字】获取Token的参数为："+userData);
        Map<String,String> paramMap = JacksonUtil.fromJson(userData,Map.class);
        Map<String,String> headerMap = JacksonUtil.fromJson(headers,Map.class);
        RequestSpecification spec = new RequestSpecBuilder()
                .addHeaders(headerMap)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .build();
        return given()
                .spec(spec)
                .params(paramMap)
                .when()
                .get(url)
                .then()
                .extract()
                .jsonPath();
    }
}

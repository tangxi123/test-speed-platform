package org.tangxi.testplatform.execution.steps.actions;

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
import org.tangxi.testplatform.execution.TestCaseWrapper;
import org.tangxi.testplatform.execution.steps.replace.ReplaceTestCase;
import org.tangxi.testplatform.execution.steps.requests.ReplaceActionHolder;
import org.tangxi.testplatform.execution.steps.requests.ReplaceSelectParam;
import org.tangxi.testplatform.mapper.DatabaseConfigMapper;
import org.tangxi.testplatform.mapper.ExecuteSqlMapper;
import org.tangxi.testplatform.mapper.PrePostActionMapper;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;
import org.tangxi.testplatform.model.prePostAction.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActionExecution {
    private static final Logger LOG = LoggerFactory.getLogger(ActionExecution.class);
    public static final List<String> logs = new ArrayList<>();

    private static final String DMLTypeRegex = "^\\b\\w+\\b"; //匹配一行的第一个单词，单词由字母数字下划线组成
    private static final String PARAM_PLACEHOLDER = "\\$\\{.*?\\}"; //匹配${*****},*表示下划线数字和字母

    private static final String PRE = "pre.";
    private static final String POST = "post.";

    private static final String SELECT = "SELECT";
    private static final String INSERT = "INSERT";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String select = "select";
    private static final String insert = "insert";
    private static final String update = "update";
    private static final String delete = "delete";

    @Autowired
    TestCaseWrapper testCaseWrapper;

    @Autowired
    PrePostActionMapper actionMapper;

    @Autowired
    DatabaseConfigMapper databaseConfigMapper;

    @Autowired
    ReplaceTestCase replaceTestCase;

    @Autowired
    ReplaceSelectParam replaceSelectParam;

    @Autowired
    ReplaceActionHolder replaceActionHolder;



    //mybatis死锁问题待解决

    /**
     * 执行测试用例的前后置动作
     * @param actionWrappers
     */
    public void execPreActions(List<PrePostActionWrapper> actionWrappers){
        Execution.logs.add("【执行前置动作】开始执行前置动作");
        if(actionWrappers.size() == 0){
            Execution.logs.add("【执行前置动作】没有可执行的前置动作");
        }
        for(PrePostActionWrapper actionWrapper : actionWrappers){
            execAction(actionWrapper,PRE);
        }
    }

    public void execPostActions(List<PrePostActionWrapper> actionWrappers){
        Execution.logs.add("【执行前置动作】开始执行后置动作");
        if(actionWrappers.size() == 0){
            Execution.logs.add("【执行前置动作】没有可执行的后置动作");
        }
        for(PrePostActionWrapper actionWrapper : actionWrappers){
            execAction(actionWrapper,POST);
        }
    }

    /**
     * 根据action类型执行前后置动作
     * @param actionWrapper
     */
    private void execAction(PrePostActionWrapper actionWrapper,String prePost){
        Execution.logs.add("【执行前置动作】需要执行的动作为："+JacksonUtil.toJson(actionWrapper));
        execActionByType(actionWrapper,prePost);
    }

    /**
     * 根据不同类型执行前后置动作
     * @param actionWrapper
     */
    private void execActionByType(PrePostActionWrapper actionWrapper,String prePost){
        PrePostActionType actionType = actionWrapper.getActionType();
        switch (actionType){
            case SQL:
                Execution.logs.add("【执行前置动作】执行的动作类型为：SQL");
                execActionBySQLType(actionWrapper,prePost);
                break;
            default:
                throw new TestCaseRunException("不存在的前后置动作类型"+actionType);
        }
    }

    /**
     * 执行sql类型的前后置动作
     * @param actionWrapper
     */
    private void execActionBySQLType(PrePostActionWrapper actionWrapper,String prePost){
        PrePostActionSql sqlAction = (PrePostActionSql)actionWrapper.getAction();
        if(sqlAction == null){
            throw new TestCaseRunException("没有可执行的sql动作"+sqlAction);
        }
        dbConExec(sqlAction,prePost);
    }

    /**
     * 根据sql配置执行前后置动作，支持DML（select insert update delete）
     * @param sqlAction
     */
    private void dbConExec(PrePostAction sqlAction,String prePost){
        String dbConfigId = sqlAction.getDbConfigId();
        List<SqlInfo> sqls = sqlAction.getSql();
        for(SqlInfo s : sqls){
            String sql = s.getSql();
            String dmlType = getDMLType(sql);
            Execution.logs.add("【执行前置动作】SQL的DML类型为："+dmlType);
            if(sql == null || dmlType == null){
                throw new TestCaseRunException("【执行前置动作】没有可执行的sql语句："+sql);
            }
            switch (dmlType){
                case INSERT:
                    execInsert(sql,prePost,dbConfigId);
                    break;
                case insert:
                    execInsert(sql,prePost,dbConfigId);
                    break;
                case UPDATE:
                    execUpdate(sql,prePost,dbConfigId);
                    break;
                case update:
                    execUpdate(sql,prePost,dbConfigId);
                    break;
                case DELETE:
                    execDelete(sql,prePost,dbConfigId);
                    break;
                case delete:
                    execDelete(sql,prePost,dbConfigId);
                    break;
                case SELECT:
                    execSelect(sql,prePost,dbConfigId);
                    break;
                case select:
                    execSelect(sql,prePost,dbConfigId);
                    break;
                default:
                    throw new UnexpectedTestCaseException("不支持的sql语句"+sql);
            }
        }

    }

    /**
     * 根据正则表达式
     * @param sql
     * @return
     */
    private String getDMLType(String sql){
        Pattern pattern = Pattern.compile(DMLTypeRegex);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()){
            return matcher.group(0);
        }
        return null;
    }

    /**
     * 执行插入操作
     * @param sql
     */
    private void execInsert(String sql,String prePost,String dbConfigId){
            String replacedSql = replaceActionHolder.replaceHolder(sql,prePost,PARAM_PLACEHOLDER);
            Execution.logs.add("【执行前置动作】执行插入操作："+replacedSql);
            DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById(dbConfigId);
            SqlSession sqlSession = SqlSessionBuilder.generateSqlSession(databaseConfig);
            ExecuteSqlMapper executeSqlMapper = sqlSession.getMapper(ExecuteSqlMapper.class);//获取执行sql的mapper
            try {
                int insert = executeSqlMapper.insert(replacedSql);
                if(insert > 0){
                    Execution.logs.add("【执行前置动作】插入操作成功，插入数量为："+insert);
                }else{
                    Execution.logs.add("【执行前置动作】插入操作失败，插入数量为："+insert);
                }
            } catch (Exception e) {
                throw new TestCaseRunException("错误的插入操作语句："+replacedSql);
            }
            sqlSession.commit();
            sqlSession.close();


    }

    /**
     * 执行更新操作
     * @param sql
     */
    private void execUpdate(String sql,String prePost,String dbConfigId){
            String replacedSql = replaceActionHolder.replaceHolder(sql,prePost,PARAM_PLACEHOLDER);
            Execution.logs.add("【执行前置动作】执行更新操作："+replacedSql);
            DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById(dbConfigId);
            SqlSession sqlSession = SqlSessionBuilder.generateSqlSession(databaseConfig);
            ExecuteSqlMapper executeSqlMapper = sqlSession.getMapper(ExecuteSqlMapper.class);//获取执行sql的mapper
            try {
                int update = executeSqlMapper.update(replacedSql);
                if(update > 0){
                    Execution.logs.add("【执行前置动作】更新操作成功，更新数量为："+update);
                }else{
                    Execution.logs.add("【执行前置动作】更新操作失败，更新数量为："+update);
                }
            } catch (Exception e) {
                throw new TestCaseRunException("错误的更新操作语句："+replacedSql);
            }
            sqlSession.commit();
            sqlSession.close();
//        }
    }

    /**
     * 执行删除操作
     * @param sql
     */
    private void execDelete(String sql,String prePost,String dbConfigId){
            String replacedSql = replaceActionHolder.replaceHolder(sql,prePost,PARAM_PLACEHOLDER);
            Execution.logs.add("【执行前置动作】执行删除操作："+replacedSql);
            DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById(dbConfigId);
            SqlSession sqlSession = SqlSessionBuilder.generateSqlSession(databaseConfig);
            ExecuteSqlMapper executeSqlMapper = sqlSession.getMapper(ExecuteSqlMapper.class);//获取执行sql的mapper
            try {
                int delete = executeSqlMapper.delete(replacedSql);
                if(delete > 0){
                    Execution.logs.add("【执行前置动作】删除操作成功，删除数量为："+delete);
                }else{
                    Execution.logs.add("【执行前置动作】删除操作失败，删除数量为："+delete);
                }
            } catch (Exception e) {
                throw new TestCaseRunException("错误的删除操作语句："+replacedSql);
            }
            sqlSession.commit();
            sqlSession.close();

    }

    /**
     * 执行SELECT操作
     * @param sql
     */
    private void execSelect(String sql,String prePost,String dbConfigId){
            String replacedSql = replaceActionHolder.replaceHolder(sql,prePost,PARAM_PLACEHOLDER);
            Execution.logs.add("【执行前置动作】执行查询操作："+replacedSql);
            DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById(dbConfigId);
            SqlSession sqlSession = SqlSessionBuilder.generateSqlSession(databaseConfig);
            ExecuteSqlMapper executeSqlMapper = sqlSession.getMapper(ExecuteSqlMapper.class);//获取执行sql的mapper
            List<LinkedHashMap<String, Object>> select = null;
            try {
                select = executeSqlMapper.select(replacedSql);
                if(select.size() > 0){
                    Execution.logs.add("【执行前置动作】查询操作成功，查询到的数据为："+JacksonUtil.toJson(select));
                }else{
                    Execution.logs.add("【执行前置动作】查询操作失败，查询数量为："+select.size());
                }
            } catch (Exception e) {
                throw new TestCaseRunException("错误的查询操作语句："+replacedSql);
            }
            LinkedHashMap<String, Object> transformedSelects = new LinkedHashMap<>();
            for(int index = 0; index<select.size(); index++){
                LinkedHashMap<String, Object> selectResult = select.get(index);
                LinkedHashMap<String, Object> transformedSelectResult = TransformSelectParamUtil.transformSelectPreParam(sql,prePost,selectResult, index);
                transformedSelects.putAll(transformedSelectResult);
            }
            testCaseWrapper.setSelectPreParams(transformedSelects);//将查询到的参数存储到testCaseMapper中
            sqlSession.close();
        }



    }

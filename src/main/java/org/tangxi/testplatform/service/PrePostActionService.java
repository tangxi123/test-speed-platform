package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.ActionDuplicateException;
import org.tangxi.testplatform.common.exception.UnexpectedActionException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.mapper.*;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;
import org.tangxi.testplatform.model.prePostAction.PrePostAction;
import org.tangxi.testplatform.model.prePostAction.PrePostActionType;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PrePostActionService {
    private static final Logger LOG = LoggerFactory.getLogger(PrePostActionService.class);

    @Autowired
    PrePostActionMapper actionMapper;

    @Autowired
    TestCaseMapper testCaseMapper;

    @Autowired
    DatabaseConfigMapper databaseConfigMapper;



    /**
     * 创建前后置动作
     *
     * @param actionWrapper
     * @return
     */
    @Transactional
    public Response<String> createAction(@RequestBody PrePostActionWrapper actionWrapper) {
        LOG.info("请求的参数为：{}", actionWrapper);
        try {
            actionWrapper.setCreateAt(LocalDateTime.now());
            actionWrapper.setUpdateAt(actionWrapper.getCreateAt());
            PrePostActionType type = actionWrapper.getActionType();
            PrePostAction action = actionWrapper.getAction();
            switch (type) {
                case SQL:
                    actionMapper.createAction(actionWrapper);
                    action.setActionId(actionWrapper.getId());
                    actionMapper.createSqlAction(action);
                    break;
            }
            return new Response<String>(200, null, "前后置动作创建成功");
        } catch (DuplicateKeyException e) {
            throw new ActionDuplicateException(actionWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 更新前后置动作actionWrapper
     *
     * @param actionWrapper
     * @return
     */
    public Response<String> updateAction(@RequestBody PrePostActionWrapper actionWrapper) {
        LOG.info("请求的参数为：{}", actionWrapper);
        try {
            actionWrapper.setUpdateAt(LocalDateTime.now());
            PrePostActionType type = actionWrapper.getActionType();
            PrePostAction action = actionWrapper.getAction();
            switch (type) {
                case SQL:
                    actionMapper.updateAction(actionWrapper);
                    action.setActionId(actionWrapper.getId());
                    actionMapper.updateSqlAction(action);
                    break;
            }
            return new Response<String>(200, null, "前后置动作更新成功");
        } catch (DuplicateKeyException e) {
            throw new ActionDuplicateException(actionWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 获取所有的前后置动作
     * @return
     */
    public Response<?> getAllActions(){
        try{
            List<PrePostActionWrapper> actionWrappers = actionMapper.getAllActions();
            return new Response<>(200,actionWrappers,"查询成功");
        }catch (Exception e){
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据id获取前后置动作
     *
     * @param id
     * @return
     */
    public Response<PrePostActionWrapper> getActionById(int id) {
        try {
            PrePostActionWrapper actionWrapper = actionMapper.getSqlActionById(id);
            LOG.info("查询到的前后置动作为{}", actionWrapper);
            return new Response<>(200, actionWrapper, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据动作名称、描述分页查询前后置动作不包括明细
     *
     * @param params
     * @return
     */
    public Response<PageInfo<PrePostActionWrapper>> getActionWrappers(Map<String, Object> params){
        try{
            int pageNum = Integer.parseInt((String)params.get("pageNum"));
            int pageSize = Integer.parseInt((String)params.get("pageSize"));
            PageHelper.startPage(pageNum,pageSize);
            List<PrePostActionWrapper> actionWrappers = actionMapper.getActionWrappers(params);
            PageInfo<PrePostActionWrapper> actions = new PageInfo<>(actionWrappers);
            return new Response<>(200,actions,"查询成功");
        }catch (Exception e){
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据动作名称、描述分页查询前后置动作
     *
     * @param params
     * @return
     */
    public Response<PageInfo<PrePostActionWrapper>> getActions(@RequestBody Map<String, Object> params) {
        try{
            int pageNum = Integer.parseInt((String) params.get("pageNum"));
            int pageSize = Integer.parseInt((String) params.get("pageSize"));
            PageHelper.startPage(pageNum, pageSize);
            List<PrePostActionWrapper> actionWrappers = actionMapper.getActionsByFields(params);
            PageInfo<PrePostActionWrapper> actions = new PageInfo<>(actionWrappers);
            return new Response<>(200,actions,"查询成功");
        }catch (Exception e){
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据id删除前后置动作
     * @param id
     * @return
     */
    public Response<String> deleteActionById(int id){
        try{
            PrePostActionWrapper actionWrapper = actionMapper.getActionById(id);
            String name = actionWrapper.getName();
            int actionCount = testCaseMapper.getActionCountByActionName('"'+name+'"');
            if(actionCount > 0){
                return new Response<>(400,null,"存在已被使用的前后置动作:"+name+"，删除失败");
            }
            int delActionCount = actionMapper.deleteActionById(id);
            if(delActionCount == 1){
                return new Response<>(200,null,"删除成功");
            }
            return new Response<>(500,null,"服务器错误");
        }catch (Exception e){
            throw new UnexpectedActionException(e);
        }
    }


    /**
     * 执行sql
     * @param actionWrapper
     * @return
     */

    public Response<?> executeSql(PrePostActionWrapper actionWrapper){

////        try{
//            String dbConfigId = actionWrapper.getAction().getDbConfigId();
//            DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById(dbConfigId);
//            String driver = databaseConfig.getDriver();
//            String host = databaseConfig.getHost();
//            int port = databaseConfig.getPort();
//            String database = databaseConfig.getDatabase();
//            String user = databaseConfig.getUser();
//            String password = databaseConfig.getPassword();
//            String url = "jdbc:mysql://"+host+":"+port+"/"+database;
//            System.out.println(url);
//            String sql = actionWrapper.getAction().getSql();
//
//
//            DriverManagerDataSource dataSource = new DriverManagerDataSource();
//            dataSource.setDriverClassName(driver);
//            dataSource.setUrl(url);
//            dataSource.setUsername(user);
//            dataSource.setPassword(password);
//
//
//            TransactionFactory trcFactory = new JdbcTransactionFactory();
//            Environment env = new Environment("development", trcFactory, dataSource);
//            Configuration config = new Configuration(env);
//            config.addMapper(ExecuteSqlMapper.class);
//
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
//            SqlSession session = sqlSessionFactory.openSession();
//            try{
//                ExecuteSqlMapper executeSqlMapper = session.getMapper(ExecuteSqlMapper.class);
//                String regex = "^\\b\\w+\\b";
//                Pattern pattern = Pattern.compile(regex);
//                Matcher matcher = pattern.matcher(sql);
//                String dml = "";
//                while(matcher.find()){
//                   dml = matcher.group();
//                }
//                switch (dml){
//                    case "SELECT" :
//                        List<LinkedHashMap<String, Object>> select = executeSqlMapper.select(sql);
//                        System.out.println(JacksonUtil.toJson(select));
//                        break;
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            return null;
////        }
    }
}

package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.UnexpectedDatabaseConfigException;
import org.tangxi.testplatform.mapper.DatabaseConfigMapper;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfigService.class);

    @Autowired
    DatabaseConfigMapper databaseConfigMapper;

    /**
     * 创建databaseConfig
     * @param databaseConfig
     * @return
     */
    public Response<?> createDatabaseConfig(DatabaseConfig databaseConfig){
        try{
            databaseConfig.setCreateAt(LocalDateTime.now());
            databaseConfig.setUpdateAt(LocalDateTime.now());
            databaseConfigMapper.createDatabaseConfig(databaseConfig);
            return new Response<>(200,null,"创建成功");
        }catch (Exception e){
            throw new UnexpectedDatabaseConfigException(e);
        }
    }

    /**
     * 更新databaseConfig配置
     * @param databaseConfig
     * @return
     */
    public Response<?> updateDatabaseConfig(DatabaseConfig databaseConfig){
        try{
            databaseConfig.setUpdateAt(LocalDateTime.now());
            databaseConfigMapper.updateDatabaseConfig(databaseConfig);
            return new Response<>(200,null,"更新成功");
        }catch (Exception e){
            throw new UnexpectedDatabaseConfigException(e);
        }
    }

    /**
     * 根据id查询databaseConfig
     * @param id
     * @return
     */
    public Response<?> getDatabaseConfigById(String id){
        try{
            DatabaseConfig databaseConfig = databaseConfigMapper.getDatabaseConfigById(id);
            return new Response<>(200,databaseConfig,"查询成功");
        }catch (Exception e){
            throw new UnexpectedDatabaseConfigException(e);
        }
    }

    public Response<?> getDatabaseConfigsByParams(Map<String,Object> params){
        try{
            int pageNum = Integer.parseInt((String)params.get("pageNum"));
            int pageSize = Integer.parseInt((String)params.get("pageSize"));
            PageHelper.startPage(pageNum, pageSize);
            List<DatabaseConfig> dbConfigs = databaseConfigMapper.getDatabaseConfigsByParams(params);
            PageInfo<DatabaseConfig> dataBaseConfigs = new PageInfo<>(dbConfigs);
            return new Response<>(200,dataBaseConfigs,"查询成功");
        }catch (Exception e){
            throw new UnexpectedDatabaseConfigException(e);
        }
    }


    /**
     * 根据模块id获取该模块下所有的数据库配置
     * @param moduleId
     * @return
     */
    public Response<?> getAllDBConfigsByModuleId(String moduleId){
        try{
            List<DatabaseConfig> allDBConfigsByModule = databaseConfigMapper.getAllDBConfigsByModuleId(moduleId);
            return new Response<>(200,allDBConfigsByModule,"查询成功");
        }catch (Exception e){
            throw new UnexpectedDatabaseConfigException(e);
        }
    }

    public Response<?> checkDBConnection(DatabaseConfig databaseConfig){
        try{
            String url = "jdbc:mysql://"+databaseConfig.getHost()+":"+databaseConfig.getPort()+"/"+databaseConfig.getDatabase();
            String user = databaseConfig.getUser();
            String password = databaseConfig.getPassword();
            Connection conn = null;
            try{
                Class.forName(databaseConfig.getDriver());
                conn = DriverManager.getConnection(url,user,password);
                return new Response<>(200,null,"连接成功");
            }catch (ClassNotFoundException e) {
                e.printStackTrace();
                return new Response<>(400,e.getMessage(),"连接失败");
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response<>(400,e.getMessage(),"连接失败");
            }finally {
                if(conn != null){
                    conn.close();
                }
            }
        }catch (Exception e){
            throw new UnexpectedDatabaseConfigException(e);
        }
    }

}

package org.tangxi.testplatform.common.util;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.tangxi.testplatform.mapper.ExecuteSqlMapper;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;

public class SqlSessionBuilder {
    public static SqlSession generateSqlSession(DatabaseConfig databaseConfig){
        String driver = databaseConfig.getDriver();
        String host = databaseConfig.getHost();
        int port = databaseConfig.getPort();
        String database = databaseConfig.getDatabase();
        String user = databaseConfig.getUser();
        String password = databaseConfig.getPassword();
        String url = "jdbc:mysql://"+host+":"+port+"/"+database;  //目前只支持jdbc，后期需要扩展
        System.out.println(url);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);


        TransactionFactory trcFactory = new JdbcTransactionFactory();
        Environment env = new Environment("development", trcFactory, dataSource);
        Configuration config = new Configuration(env);
        config.addMapper(ExecuteSqlMapper.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
        SqlSession session = sqlSessionFactory.openSession();


        return session;
    }
}

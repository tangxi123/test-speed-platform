package org.tangxi.testplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.tangxi.testplatform.model.databaseConfig.DatabaseConfig;

import javax.sql.DataSource;

//@Configuration
public class DataBaseConfig {
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setUrl();
        return null;
    }

    @Bean
    public DatabaseConfig databaseConfig(){
        return new DatabaseConfig();
    }
}

package com.yang.beetl.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    
    @Bean
    public DataSource dataSource(Environment env){
        HikariDataSource db = new HikariDataSource();
        db.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        db.setJdbcUrl(env.getProperty("spring.datasource.url"));
        db.setUsername(env.getProperty("spring.datasource.username"));
        db.setPassword(env.getProperty("spring.datasource.password"));
        return db;
    }
}

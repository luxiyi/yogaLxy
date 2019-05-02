package com.woniu.yago.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 19:35
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("regByPhone.html");
        registry.addViewController("regByEmail.html");
        registry.addViewController("regPc.html");
        registry.addViewController("loginApp.html");
        registry.addViewController("loginPc.html");
        registry.addViewController("indexPc.html");
        registry.addViewController("index.html");
        registry.addViewController("findPwd.html");
        registry.addViewController("updateUserPwd.html");
        registry.addViewController("studentInfo.html");
        registry.addViewController("bingPhone.html");
        registry.addViewController("coachInfo.html");
        registry.addViewController("privacy.html");
        registry.addViewController("accountSafe.html");
        registry.addViewController("modifyUserPwd.html");

    }
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager manager(DataSource dataSource){
        DataSourceTransactionManager manager= new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:D:/temp-rainy/");
    }



}

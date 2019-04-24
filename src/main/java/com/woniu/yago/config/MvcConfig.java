package com.woniu.yago.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        registry.addViewController("login.html");
        registry.addViewController("index.html").setViewName("index");
        registry.addViewController("findPwd.html");
        registry.addViewController("updateUserPwd.html");
    }
}

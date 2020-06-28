package com.hcycom.sso.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
 
/*    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LogIPFilter());
        registration.addUrlPatterns("/*");
        registration.setName("LogIPFilter");
        registration.setOrder(1);
        return registration;
    }*/
    
    /**  
    * @Title: addHeaderFilter  
    * @Description: 过滤没走api，加跨域   
    * @author 李凯 
    * @date 2018年12月20日
    */ 
    @Bean
    public FilterRegistrationBean addHeaderFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CrossDomainFilter());
        registration.addUrlPatterns("/auth/login");
        registration.setName("CrossDomainFilter");
        registration.setOrder(2);
        return registration;
    }
 
}
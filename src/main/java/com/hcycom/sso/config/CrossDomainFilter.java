package com.hcycom.sso.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author 李凯
 * @ClassName: CrossDomainFilter
 * @Description: 处理部分接口的跨域
 * @date 2018年12月20日
 */
public class CrossDomainFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        //System.out.println("21312839jhklllllllllllllllllllllllllllllllllllllllllll");
        //response.addHeader("Access-Control-Allow-Origin", "*");//http://192.168.0.112:8081
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, authorization");
        filterChain.doFilter(request, response);
    }
}

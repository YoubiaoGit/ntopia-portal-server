package com.hcycom.sso.OperationLog;


import com.hcycom.sso.dto.UserDTO;
import com.hcycom.sso.service.UaaFeignClient;
import com.hcycom.sso.utils.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Path：
 * @Classname：
 * @Description：
 * @Author：yandi
 * @CreateTime：2018/9/17 12:17
 * @ModifyUser：yandi
 * @ModifyRemark：
 * @ModifyTime：2018/9/17 12:17
 */
@Slf4j
@Aspect
@Component
public class SysOperationLogAspect {
	@Autowired
    private UaaFeignClient feignClient;

    @Pointcut("@annotation(com.hcycom.sso.OperationLog.OperationLog)")
    public void logCut() {

    }

    //切面 配置通知
    @AfterReturning("logCut()")
    public void saveSysLog(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteAddr = HttpRequestUtil.getIpAddr(request);
        String serverAddr = request.getRequestURL().toString() + "?";
        //截取问号之前，去掉参数
        serverAddr = serverAddr.substring(0, serverAddr.indexOf("?"));
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        String title = className + "-" + methodName;
        //获取操作--注解中传递的参数
        OperationLog myLog = method.getAnnotation(OperationLog.class);
        String value = "";
        if (null != myLog) {
            value = myLog.value();
        }
        
        //用户id
        UserDTO userDTO= feignClient.account();
        String userId ="";
		if (userDTO != null && userDTO.getId() != null){
			userId = userDTO.getId().toString();		
		}
        System.out.println("remoteAddr:" + remoteAddr + ",serverAddr:" + serverAddr + ",className:" + className + ",methodName:" + methodName);
        //防止标识符重复，命名复杂点
        log.info("operation_log_data_remoteAddr_userid_serverAddr:[{},{},{},{}]",
                remoteAddr, userId, serverAddr, value);
    }

    private String getUserId(HttpServletRequest request) {
        String userid = request.getParameter("userId");
        if (StringUtils.isEmpty(userid)) {
            Cookie[] c = request.getCookies();
            if (c != null) {
                for (Cookie co : c) {
                    if ("userid".equals(co.getName())) {
                        userid = co.getValue();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isEmpty(userid)) {
            userid = request.getHeader("userid");
        }

        return userid;
    }

}

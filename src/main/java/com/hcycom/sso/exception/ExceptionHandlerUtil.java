package com.hcycom.sso.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 *
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 18:17
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerUtil {
    final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtil.class);
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e){
        e.printStackTrace();
        //String jsonpCallback = request.getParameter("jsonpCallback");// 客户端请求参数
        //logger.error("jsonpCallback exceptionHandler---->" + jsonpCallback);

        logger.error("ExceptionHandler   msg-->"+e.getMessage(), e);
        if(e instanceof SsoException) {
            SsoException ce = (SsoException) e;
            return   ResultVOUtil.error(1, ce.getMessage());
        }  else if (e instanceof  BindException){
            BindException be = (BindException) e;
            ObjectError error = be.getAllErrors().get(0);
            return ResultVOUtil.error(1, String.format(error.getDefaultMessage()));
        } else {
            return ResultVOUtil.error(1, "服务端错误");
        }
    }
}

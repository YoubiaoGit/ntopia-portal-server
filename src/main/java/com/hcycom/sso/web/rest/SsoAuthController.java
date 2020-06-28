/*package com.hcycom.sso.web.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.SysLoginLog;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.SysLoginlogService;
import com.hcycom.sso.utils.ExpiryMapUtil;
import com.hcycom.sso.utils.HttpRequestUtil;
import com.hcycom.sso.utils.IPLocationUtil;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.utils.UUIDTool;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.UserInfoVO;

import tk.mybatis.mapper.util.StringUtil;

*//**
 *
 * sso认证
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-18 14:31
 *//*
@Api(tags = { "sso认证接口" }, description = "SsoAuthController")
@Controller
@RequestMapping("/api/sso")
public class SsoAuthController {
    final static Logger logger = LoggerFactory.getLogger(SsoAuthController.class);
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysLoginlogService sysLoginlogService;
    // key:token value:登录用户id  有效期30分钟

//    @Value("${login_url}")

    //    private String login_url;
    //private static final Map<String, ResultVO<SysUser>> USER = new HashMap<>();
    public static ExpiryMapUtil<String,String> onlineUserMap = new ExpiryMapUtil<>(1000 * 60 * 30);
    public static final Map<String, String> USER_LOGIN_NAME = new HashMap<>();
    public static final ExpiryMapUtil<String,String> expiryMap = new ExpiryMapUtil<>(1000 * 60 * 60 * 24 * 7);


    //SSO的登录
    @ApiOperation(value = "登录系统实现", notes = "登录系统")
    @ApiImplicitParam(name = "map", value = "集合", dataType = "ModelMap")
    @OperationLog(value = "登录系统")
    @RequestMapping(value="ssologin", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object ssologin(HttpServletRequest request, HttpServletResponse response, @ApiParam("认证信息集合")ModelMap map) {
        String ip = HttpRequestUtil.getIpAddr(request);

        logger.info("ip 为"+ip+" 进入了ssologin方法....");

        ResultVO<SysUser> r  = this.sysUserService.checkUser(request, response);
        System.out.println("---当前用户信息--"+r);





        return null;

    }




    //SSO的退出
    @ApiOperation(value = "退出系统实现", notes = "退出系统")
    @OperationLog(value = "退出系统")
    @GetMapping("logout")
    @ResponseBody
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        String ip = HttpRequestUtil.getIpAddr(request);
        logger.info("ip为"+ip+" 进入了退出方法......");
        //String url = request.getParameter("goto");
        String jsonpCallback = request.getParameter("jsonpCallback");// 客户端请求参数

        //List<String> list = PropertiesUtil.getLogoutHost();
        UserInfoVO u = sysUserService.getRedisUser();
        if(u != null) {
            USER_LOGIN_NAME.remove(u.getTelephone());
        }

        //退出登录 记录时间
        String token = sysUserService.getToken(request);
        if (!StringUtil.isEmpty(token)) {
            updateLoginLogDate(token);
            expiryMap.remove(token);//登出时间记录后清除该条数据
            expiryMap.entrySet();
            sysUserService.clearCookies(request, response);
        }

        if(StringUtils.isEmpty(jsonpCallback)) {
            return ResultVOUtil.success();
        }

        return jsonpCallback + "(" + JSON.toJSONString(ResultVOUtil.success()) + ")";
    }


    *//**
     * @Method: updateLoginLogDate
     * @Description:   更新登陆日志登出时间
     * @Param: [token]
     * @Author: zh
     * @Date: 2019/1/17 17:13
     * @return: void
     *//*
    @ApiOperation(value = "更新登录日志登出时间", notes = "更新登录日志登出时间")
    @ApiImplicitParam(name = "token", value = "token", dataType = "String")
    @GetMapping(value="/updateLogTime")
    @ResponseBody
    public void updateLoginLogDate(@RequestParam @ApiParam("token认证")String token){

        //循环判断移除失效用户
        for (Map.Entry<String, String> entry : onlineUserMap.entrySet()) {
            if(entry.getValue().equals(token)){
                //表示该用户下线
                onlineUserMap.remove(entry.getKey());
            }
        }

        //更新登录日志退出时间
        Date endTime = new Date();
        String loginLogId = expiryMap.get(token);
        if(loginLogId!=null){
            sysLoginlogService.updateLog(loginLogId, endTime);
        }
    }

}
*/
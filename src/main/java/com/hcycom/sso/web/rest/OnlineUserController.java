/*package com.hcycom.sso.web.rest;

import com.alibaba.fastjson.JSON;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.OnlineUser;
import com.hcycom.sso.service.permissions.OnlineUserService;
import com.hcycom.sso.utils.ExpiryMapUtil;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//**
 * @ClassName: OnlineUserController
 * @Description: TODO
 * @auther: zh
 * @date: 2018/6/27 21:10
 * @version: 1.0
 *//*
@Api(tags= {"在线用户统计接口"})
@Slf4j
@RestController
@RequestMapping("/api/onlineUser")
public class OnlineUserController {

    @Autowired
    private OnlineUserService onlineUserService;

    *//**
     * @Title: findAll
     * @Description: TODO
     * @Author: zh
     * @Date: 22:27 2018/6/27
     * @Param: [keyWord]
     * @return: com.hcycom.mc.log.vo.ResultVO<java.util.List       <       com.hcycom.mc.log.domain.OnlineUser>>
     *//*
    @ApiOperation(value = "查询在线用户列表")
    @OperationLog(value = "监控系统-在线用户列表")
    @CrossOrigin
    @GetMapping("/findAll")
    public ResultVO<List<OnlineUser>> findAll(@RequestParam(required = false) @ApiParam(value="查询关键词")String keyWord) {
        log.info("[调用controller开始，入参：{}]");
        try {
            Map<String, String> map = new HashMap();
            //获取在线用户token
            ExpiryMapUtil<String, String> onlineUserMap = SsoAuthController.onlineUserMap;
            //获取登录日志id
            ExpiryMapUtil<String, String> loginLogMap = SsoAuthController.expiryMap;
            if (onlineUserMap != null && onlineUserMap.size() > 0) {
                for (Map.Entry<String, String> entry : onlineUserMap.entrySet()) {
                    String token = entry.getValue();
                    String s = loginLogMap.get(token);
                    map.put(token, s);
                }
            }


            if (map != null && map.size() > 0) {
                List<OnlineUser> users = onlineUserService.findAll(keyWord, map);
                log.info("[调用controller结束，出参：{}]", JSON.toJSON(users));
                return ResultVOUtil.success(users);
            } else {
                log.info("[调用controller结束，出参：{}]");
                return ResultVOUtil.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }
    }
}
*/
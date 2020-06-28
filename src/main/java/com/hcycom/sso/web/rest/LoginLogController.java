package com.hcycom.sso.web.rest;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.LoginLog;
import com.hcycom.sso.domain.SysLoginLog;
import com.hcycom.sso.dto.LoginLogCountDto;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.LoginLogService;
import com.hcycom.sso.service.permissions.SysLoginlogService;
import com.hcycom.sso.utils.HttpRequestUtil;
import com.hcycom.sso.utils.IPLocationUtil;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.utils.UUIDTool;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.UserInfoVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: LoginLogController
 * @Description: TODO
 * @auther: lr
 * @date: 2018/6/27 21:10
 * @version: 1.0
 */
@Api(tags = {"登录日志接口"})
@Slf4j
@RestController
@RequestMapping("/api/loginLog")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysLoginlogService sysLoginlogService;

    /**
     * @Title: findAll
     * @Description: 查询所有用户的登录日志
     * @Author: zh
     * @Date: 9:17 2018/6/28
     * @Param: [keyWord, pageNum, pageSize]
     * @return: com.hcycom.mc.log.vo.ResultVO<java.util.List < com.hcycom.mc.log.domain.LoginLog>>
     */
    @ApiOperation(value = "查询所有用户的登录日志")
    @OperationLog(value = "系统监控-登录日志列表")
    @CrossOrigin
    @GetMapping("/findAll")
    public ResultVO<List<LoginLog>> findAll(@RequestParam(required = false) @ApiParam(value = "查询关键词") String keyWord,
                                            @RequestParam(required = false, defaultValue = "0") @ApiParam(value = "页码") int pageNum,
                                            @RequestParam(required = false, defaultValue = "0") @ApiParam(value = "每页条数") int pageSize,
                                            @RequestParam(required = false) @ApiParam(value = "开始时间") String startTime,
                                            @RequestParam(required = false) @ApiParam(value = "结束时间") String endTime) {
        log.info("[调用controller开始，入参：keyWord:{},pageNum:{},pageSize:{},startTime:{},endTime:{}]");
        try {
            if (pageNum == 0 && pageSize == 0) {
                List<LoginLog> list = loginLogService.findAll(keyWord, startTime, endTime);
                log.info("[调用controller结束，出参：{}]", JSON.toJSON(list));
                return ResultVOUtil.success(list);
            } else {
                PageInfo<LoginLog> loginInfo = loginLogService.findAllByPage(keyWord, pageNum, pageSize, startTime, endTime);
                log.info("[调用controller结束，出参：{}]", JSON.toJSON(loginInfo));
                return ResultVOUtil.success(loginInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }

    }

    /**
     * @Title: findOne
     * @Description: 查找单个用户的某段时间内的登录日志
     * @Author: zh
     * @Date: 9:54 2018/6/28
     * @Param: [keyWord, pageNum, pageSize, id]
     * @return: com.hcycom.mc.log.vo.ResultVO<com.hcycom.mc.log.domain.LoginLog>
     */
    @ApiOperation(value = "查找单个用户的某段时间内的登录日志")
    @OperationLog(value = "监控系统-登录日志详情")
    @CrossOrigin
    @GetMapping("/findOne")
    public ResultVO<LoginLog> findOne(@RequestParam @ApiParam(value = "用户id") String id) {
        log.info("[调用controller开始，入参：id:{}]", id);
        try {
            LoginLog loginLog = loginLogService.findOne(id);
            log.info("[调用controller结束，出参：{}]", JSON.toJSON(loginLog));
            return ResultVOUtil.success(loginLog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }

    }

    @ApiOperation(value = "查询登录日志登录次数")
    @OperationLog(value = "监控系统-登录日志登录次数")
    @CrossOrigin
    @GetMapping("/queryCount")
    public ResultVO<Map<String, Object>> queryCount(@RequestParam @ApiParam(value = "开始时间") String startTime,
                                                    @RequestParam @ApiParam(value = "结束时间") String endTime) {
        log.info("[调用controller开始，入参：startTime:{},endTime:{}]");
        Map<String, Object> map = new HashMap<>();
        try {
            LoginLogCountDto logCountDto = loginLogService.queryCount(startTime, endTime);
            map.put("todayCount", logCountDto.getTodayCount());
            map.put("dayRatio", logCountDto.getDayRatio());
            map.put("weekRatio", logCountDto.getWeekRatio());
            log.info("[调用controller结束，出参：{}]", JSON.toJSON(map));
            return ResultVOUtil.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }
    }

    @ApiOperation(value = "查询登录日志城市登录次数")
    @OperationLog(value = "系统-登录日志城市登录次数")
    @CrossOrigin
    @GetMapping("/queryCountByCity")
    public ResultVO<List<Map<String, Object>>> queryCountByCity(@RequestParam @ApiParam(value = "开始时间") String startTime,
                                                                @RequestParam @ApiParam(value = "结束时间") String endTime) {
        log.info("[调用controller开始，入参：startTime:{},endTime:{}]");
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            List<LoginLogCountDto> logCountDtoList = loginLogService.queryCountByCity(startTime, endTime);
            for (int i = 0; i < logCountDtoList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("city", logCountDtoList.get(i).getCity());
                map.put("count", logCountDtoList.get(i).getCount());
                list.add(map);
            }
            log.info("[调用controller结束，出参：{}]", JSON.toJSON(list));
            return ResultVOUtil.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }
    }

    //登录日志记录
    @ApiOperation(value = "登录日志记录")
    @GetMapping(value = "/setloginlog")
    public ResultVO setloginlog(HttpServletRequest request, HttpServletResponse response) {
        UserInfoVO user = this.sysUserService.getRedisUser();
        String ip = HttpRequestUtil.getIpAddr(request);

        if (user.getId() != null) {
            sysLoginlogService.updateLogbyIp(ip, user.getId(), new Date());
            SysLoginLog sysLoginLog = new SysLoginLog();
            String id = UUIDTool.getUUID();
            String uid = user.getId();
            String fullName = user.getFullName();
            Date date = new Date();
            sysLoginLog.setId(id);
            sysLoginLog.setSysUserId(uid);
            sysLoginLog.setIpAddr(ip);
            sysLoginLog.setCreateUser(fullName);
            sysLoginLog.setStartTime(date);
            String remoteAddr = HttpRequestUtil.getIpAddr(request);
            //获取ip归属
            IPLocationUtil ipLocationUtil = new IPLocationUtil();
            String addr = ipLocationUtil.getAddresses(remoteAddr);
            String ipLocation = "";
            String isp = "";
            String[] arr = addr.split("\\s+");
            ipLocation = arr[0];
            if (arr.length > 1) {
                isp = arr[1];
            }
            sysLoginLog.setIpLocation(ipLocation);
            sysLoginLog.setIsp(isp);
            sysLoginlogService.saveLog(sysLoginLog);
        }
        return ResultVOUtil.success("");
    }

    //退出登录日志记录
    @ApiOperation(value = "退出登录日志记录")
    @GetMapping(value = "/setlogoutlog")
    public ResultVO setlogoutlog(HttpServletRequest request, HttpServletResponse response) {
        UserInfoVO user = this.sysUserService.getRedisUser();
        String ip = HttpRequestUtil.getIpAddr(request);

        if (user.getId() != null) {
            sysLoginlogService.updateLogbyIp(ip, user.getId(), new Date());

        }
        return ResultVOUtil.success("");
    }

}

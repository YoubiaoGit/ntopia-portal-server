package com.hcycom.sso.service.permissions.impl;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.dao.permissions.LoginLogDao;
import com.hcycom.sso.domain.LoginLog;
import com.hcycom.sso.dto.LoginLogCountDto;
import com.hcycom.sso.service.permissions.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: LoginLogServiceImpl
 * @Description: TODO
 * @auther: zh
 * @date: 2018/6/27 20:57
 * @version: 1.0
 */
@Slf4j
@Service
public class LoginLogServiceImpl implements LoginLogService {


    @Autowired
    private LoginLogDao loginLogDao;

    /**
     * @Title: findAllByPage
     * @Description: 查询所有用户的登录日志
     * @Author: lr
     * @Date: 21:41 2018/6/27
     * @Param: [keyWord, pageNum, pageSize,startTime,endTime]
     * @return: com.github.pagehelper.PageInfo<com.hcycom.mc.log.domain.LoginLog>
     */
    @Override
    public PageInfo<LoginLog> findAllByPage(String keyWord, int pageNum, int pageSize, String startTime, String endTime) {
        log.info("[调用service开始，入参：keyWord:{},pageNum:{},pageSize:{},startTime:{},endTime:{}]",keyWord,pageNum,pageSize,startTime,endTime);
        PageHelper.startPage(pageNum, pageSize);
        List<LoginLog> list = findAll(keyWord,startTime,endTime);
        PageInfo<LoginLog> epi = new PageInfo<>(list);
        log.info("[调用service结束，出参：epi:{}]", JSON.toJSON(epi));
        return epi;
    }

    /**
     * @Title: findAll
     * @Description: 查询所有用户的登录日志
     * @Author: zh
     * @Date: 21:41 2018/6/27
     * @Param: [keyWord, startTime, endTime]
     * @return: java.util.List<com.hcycom.mc.log.domain.LoginLog>
     */
    @Override
    public List<LoginLog> findAll(String keyWord,String startTime,String endTime) {
        log.info("[调用service开始，入参：keyWord:{},startTime:{},endTime:{}]",keyWord,startTime,endTime);
        List<String> keyWords = null;
        if(!StringUtils.isEmpty(keyWord)){
            String[] split = keyWord.split("\\s+");
            keyWords = Arrays.asList(split);
        }
        List<LoginLog> list = loginLogDao.findAll(keyWords,startTime,endTime);
        log.info("[调用service结束，出参：list:{}]", JSON.toJSON(list));
        return list;
    }

    /**
     * @Title: findOne
     * @Description: 查找单个用户的某段时间内的登录日志
     * @Author: zh
     * @Date: 15:31 2018/6/28
     * @Param: [id, pageNum, pageSize, startTime, endTime]
     * @return: com.github.pagehelper.PageInfo<com.hcycom.mc.log.domain.LoginLog>
     */


    @Override
    public LoginLog findOne(String id) {
        log.info("[调用service开始，入参：id:{}]", id);
        LoginLog loginLog = loginLogDao.findOne(id);
        log.info("[调用service结束，出参：logDto:{}]", JSON.toJSON(loginLog));
        return loginLog;
    }

    /**
     * @Title: queryCount
     * @Description: 查询今日登陆次数、日环比、周同比
     * @Author: zh
     * @Date: 9:15 2018/6/29
     * @Param: [startTime,endTime]
     * @return: com.hcycom.mc.log.dto.LoginLogCountDto
     */

    @Override
    public LoginLogCountDto queryCount(String startTime, String endTime){
        log.info("[调用service开始，入参：startTime:{},endTime:{}]");
        LoginLogCountDto logDto = loginLogDao.queryCount( startTime,endTime);
        log.info("[调用service结束，出参：logDto:{}]", JSON.toJSON(logDto));
        return logDto;
    }

    /**
     * @Title: queryCountByCity
     * @Description: 按各地级城市查询登录次数
     * @Author: zh
     * @Date: 9:16 2018/6/29
     * @Param: [startTime,endTime]
     * @return: java.util.List<com.hcycom.mc.log.dto.LoginLogCountDto>
     */

    @Override
    public List<LoginLogCountDto> queryCountByCity(String startTime,String endTime) {
        log.info("[调用service开始，入参：startTime:{},endTime:{}]");
        List<LoginLogCountDto> list=loginLogDao.queryCountByCity(startTime,endTime);
        log.info("[调用service结束，出参：list:{}]", JSON.toJSON(list));
        return list;
    }

}

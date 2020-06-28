package com.hcycom.sso.service.permissions.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.dao.permissions.OperationLogDao;
import com.hcycom.sso.domain.OperationLog;
import com.hcycom.sso.service.permissions.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;


/**
 * @ClassName: OperationLogServiceImpl
 * @Description: TODO
 * @auther: zh
 * @date: 2018/6/27 20:57
 * @version: 1.0
 */
@Slf4j
@Service
public class OperationLogServiceImpl implements OperationLogService {


    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * @Title: findAllByPage
     * @Description: 查找所有操作日志
     * @Author: zh
     * @Date: 21:01 2018/6/27
     * @Param: [keyWord, pageNum, pageSize]
     * @return: com.github.pagehelper.PageInfo<com.hcycom.mc.log.domain.OperationLog>
     */
    @Override
    public PageInfo<OperationLog> findAllByPage(String keyWord, int pageNum, int pageSize,String startTime, String endTime) {
        log.info("[调用service开始，入参：keyWord:{},pageNum:{},pageSize:{}]", keyWord,pageNum,pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<OperationLog> list = findAll(keyWord, startTime, endTime);
        PageInfo<OperationLog> epi = new PageInfo<>(list);
        log.info("[调用service结束，出参：epi:{}]", JSON.toJSON(epi));
        return epi;
    }

    /**
     * @Title: findAll
     * @Description: 查找所有操作日志
     * @Author: zh
     * @Date: 21:01 2018/6/27
     * @Param: [keyWord]
     * @return: java.util.List<com.hcycom.mc.log.domain.OperationLog>
     */
    @Override
    public List<OperationLog> findAll(String keyWord, String startTime, String endTime) {
        log.info("[调用service开始，入参：keyWord:{}]", keyWord);
        List<String> keyWords = null;
        if(!StringUtils.isEmpty(keyWord)){
            String[] split = keyWord.split("\\s+");
            keyWords = Arrays.asList(split);
        }
        List<OperationLog> list = operationLogDao.findAll(keyWords, startTime, endTime);
//        log.info("[调用service结束，出参：list:{}]", JSON.toJSON(list));
        return list;
    }

    /**
     * @Title: findOne
     * @Description: 查找单个操作日志
     * @Author: zh
     * @Date: 21:02 2018/6/27
     * @Param: [id]
     * @return: com.hcycom.mc.log.domain.OperationLog
     */

    @Override
    public OperationLog findOne(String id) {
        log.info("[调用service开始，入参：id:{}]", id);
        OperationLog operationLog = operationLogDao.findOne(id);
        log.info("[调用service结束，出参：operationLog:{}]", JSON.toJSON(operationLog));
        return operationLog;
    }
}

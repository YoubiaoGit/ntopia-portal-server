package com.hcycom.sso.utils;

import com.hcycom.sso.dao.permissions.SysLoginLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskUtil {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ScheduleTaskUtil.class);

    @Autowired
    private SysLoginLogDao sysLoginLogDao;

    public void run() {
        LOGGER.info("《==定时任务开始-更新登录日志中失效用户的的退出时间==》");
        int i = sysLoginLogDao.updateLoginOutDate();
        LOGGER.info("《==定时任务结束-更新登录日志中失效用户的的退出时间-更新了"+i+"条数据==》");
    }
}

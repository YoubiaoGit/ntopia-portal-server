package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.dao.permissions.SysLoginLogDao;
import com.hcycom.sso.domain.SysLoginLog;
import com.hcycom.sso.service.permissions.SysLoginlogService;
import com.hcycom.sso.utils.UUIDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author:lirong
 * @Date:Created
 * 登录日志
 */
@Service
public class SysLoginLogServiceImpl implements SysLoginlogService{

    @Autowired
    private SysLoginLogDao sysLoginLogDao;
    @Override
    public boolean saveLog(SysLoginLog sysLoginLog) {
        String uuid = UUIDTool.getUUID();
        sysLoginLog.setId(uuid);
        return sysLoginLogDao.insertLog(sysLoginLog)>0;
    }

    @Override
    public boolean updateLog(String id, Date endTime) {
        return sysLoginLogDao.update(id,endTime)>0;
    }
    
    @Override
    public boolean updateLogbyIp(String ip,String uid, Date endTime) {
        return sysLoginLogDao.updatebyIp(ip,uid,endTime)>0;
    }
    @Override
    public int updateLoginOutDate() {
        int rows = sysLoginLogDao.updateLoginOutDate();
        return rows;
    }

}

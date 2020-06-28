package com.hcycom.sso.service.permissions;

import com.hcycom.sso.dao.permissions.SysLoginLogDao;
import com.hcycom.sso.domain.SysLoginLog;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface SysLoginlogService {
    boolean saveLog(SysLoginLog sysLoginLog);
    boolean updateLog(String id,Date endTime);
    boolean updateLogbyIp(String ip,String uid, Date endTime);
    int updateLoginOutDate();
}

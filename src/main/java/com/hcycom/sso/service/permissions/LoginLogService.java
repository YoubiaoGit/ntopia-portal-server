package com.hcycom.sso.service.permissions;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.domain.LoginLog;
import com.hcycom.sso.dto.LoginLogCountDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LoginLogService {
    PageInfo<LoginLog> findAllByPage(String keyWord, int pageNum, int pageSize, String startTime, String endTime);

    List<LoginLog> findAll(String keyWord, String startTime, String endTime);

    LoginLog findOne(String id);

    LoginLogCountDto queryCount(String startTime, String endTime);

    List<LoginLogCountDto> queryCountByCity(String startTime,String endTime);
}

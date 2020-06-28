package com.hcycom.sso.service.permissions;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.domain.OperationLog;

import java.util.List;

/**
 * @Author:
 * @Date:Created
 */
public interface OperationLogService {
    PageInfo<OperationLog> findAllByPage(String keyWords, int pageNum, int pageSize, String startTime, String endTime);

    List<OperationLog> findAll(String keyWords, String startTime, String endTime);

    OperationLog findOne(String id);
}

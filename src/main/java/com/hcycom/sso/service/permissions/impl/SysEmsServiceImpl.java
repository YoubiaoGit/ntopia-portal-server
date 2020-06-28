package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.dao.permissions.SysEmsDao;
import com.hcycom.sso.domain.SysEms;
import com.hcycom.sso.service.permissions.SysEmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:
 * @Date:Created
 */
@Service
public class SysEmsServiceImpl implements SysEmsService{

    @Autowired
    private SysEmsDao sysEmsDao;
    @Override
    public List<SysEms> findAll() {
        return sysEmsDao.findAll();
    }

    @Override
    public SysEms findByOne(String id) {
        return sysEmsDao.findOne(id);
    }
}

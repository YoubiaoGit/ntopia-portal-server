package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.dao.permissions.SysEmsDao;
import com.hcycom.sso.dao.permissions.SysSpecialtyDao;
import com.hcycom.sso.domain.SysSpecialty;
import com.hcycom.sso.service.permissions.SysSpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:lirong
 * @Date:Created
 */
@Service
public class SysSpecialtySeviceImpl implements SysSpecialtyService{

    @Autowired
    private SysSpecialtyDao sysSpecialtyDao;
    @Override
    public List<SysSpecialty> findAll() {
        return sysSpecialtyDao.findAll();
    }

    @Override
    public SysSpecialty findOne(String id) {
        return sysSpecialtyDao.findOne(id);
    }
}

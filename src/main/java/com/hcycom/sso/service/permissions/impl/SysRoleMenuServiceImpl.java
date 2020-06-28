package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.dao.permissions.SysRoleMenuDao;
import com.hcycom.sso.domain.SysRoleMenu;
import com.hcycom.sso.service.permissions.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-20 13:04
 */
@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    public List<SysRoleMenu> findMenuIdByRoleid(String id) {
        return sysRoleMenuDao.findMenuIdByRoleid(id);
    }
}

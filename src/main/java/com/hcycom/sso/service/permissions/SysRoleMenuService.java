package com.hcycom.sso.service.permissions;

import com.hcycom.sso.domain.SysRoleMenu;

import java.util.List;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-20 12:21
 */
public interface SysRoleMenuService {
    List <SysRoleMenu> findMenuIdByRoleid(String id);
}

package com.hcycom.sso.service.permissions;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.dto.RoleDTO;

import java.util.List;

public interface SysRoleService {
    PageInfo<RoleDTO> findByPage(String search, int start, int pageSize);
    SysRole findById(String id);
    boolean deleteRole(String id);
    boolean insertRole(SysRole role);
    List<SysRole> findRoleName(String id);
    List<String> findRoleids(String id);
    boolean updateRole(SysRole role);
    List<SysRole> findByUid(String uid);
    List<String> userByRole(String id);

    /**
     * 添加或修改角色时，查询角色名称是否已经存在
     * @param id
     * @param name
     * @return
     */
    boolean isExistRoleName(String id, String name);
}

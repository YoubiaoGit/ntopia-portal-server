package com.hcycom.sso.service.permissions.impl;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.cache.SysRoleCache;
import com.hcycom.sso.dao.permissions.SysRoleDao;
import com.hcycom.sso.dao.permissions.SysRoleMenuDao;
import com.hcycom.sso.dao.permissions.SysRoleUserDao;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.dto.OrganDTO;
import com.hcycom.sso.dto.RoleDTO;
import com.hcycom.sso.exception.SsoException;
import com.hcycom.sso.service.BaseFindPageService;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.SysRoleService;
import com.hcycom.sso.utils.CommonUtil;
import com.hcycom.sso.utils.UUIDTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseFindPageService<RoleDTO> implements SysRoleService {
    final static Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysRoleUserDao sysRoleUserDao;

    @Override
//    @Cacheable(value = SysRoleCache.CACHE_FIND_ROLE_PAGE)
    public PageInfo<RoleDTO> findByPage(String search, int start, int pageSize) {
        return findByPage(search, start, pageSize,sysRoleDao);
    }

   /* public SysRole findById(String id) {
        return sysRoleDao.selectByPrimaryKey(id);
    }*/
    public SysRole findById(String id) {
    	SysRole role = sysRoleDao.selectByPrimaryKey(id);
    	List<String> menuids = sysRoleDao.selectMenuidsByRoleid(id);
    	if(!menuids.isEmpty()) {
        	role.setMenuids(menuids);
    	}
        return role;
    }


    @CacheEvict(value = SysRoleCache.CACHE_FIND_ROLE_PAGE, allEntries = true)
    public boolean deleteRole(String id) {
        sysRoleMenuDao.deleteByRoleid(id);//先删除角色关联的id
        int i = sysRoleDao.deleteByPrimaryKey(id);
        /*try {
            i = sysRoleDao.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new SsoException("该记录关联其他数据, 不能删除");
        }*/
        return i > 0;
    }

    public List<String> findRoleids(String id) {
        return sysRoleUserDao.findByRoleid(id);
    }

    public List<SysRole> findRoleName(String id) {
        List<SysRole> roles = sysRoleDao.findRoleName();
        if(StringUtils.isEmpty(id)){
            return roles;
        }
        List<String> roleid = sysRoleUserDao.findByRoleid(id);
        for (SysRole role : roles) {
            for (String rid : roleid) {
                if(rid.equals(role.getId())) {
                    role.setChecked(true);
                    break;
                }
            }
        }
        return roles;
    }

//    @CacheEvict(value = SysRoleCache.CACHE_FIND_ROLE_PAGE, allEntries = true)
    public boolean insertRole(SysRole role) {
        String uuid = UUIDTool.getUUID();
        role.setId(uuid);
        Integer maxSort = sysRoleDao.getMaxSort();
        role.setSort(maxSort+1);
        int i = sysRoleDao.insert(role);
        logger.info("roleid-->" + role.getId());
        List<String> s = role.getMenuids();

        if(!StringUtils.isEmpty(s) && i > 0) {
            List<String> list = CommonUtil.idLists(role.getId(), s);
            int x = sysRoleMenuDao.insertBatch(list);
        }
        return i > 0;
    }

    @Override
    @CacheEvict(value = SysRoleCache.CACHE_FIND_ROLE_PAGE, allEntries = true)
    public boolean updateRole(SysRole role) {
        int  i = sysRoleDao.updateByPrimaryKeySelective(role);
        List<String> s = role.getMenuids();
        if(i > 0 && s != null && s.size() > 0) {
            List<String> list = CommonUtil.idLists(role.getId(), s);
            /**
             * 先删除再添加
             */
            sysRoleMenuDao.deleteByRoleid(role.getId());
            int x = sysRoleMenuDao.insertBatch(list);
        }
        return i > 0;
    }

    /**
     * 根据用户id查询角色
     * @param uid
     * @return
     */
    @Override
    public List<SysRole> findByUid(String uid) {
        List<SysRole>  list = sysRoleDao.findByUid(uid);
        return list;
    }

    @Override
    public List<String> userByRole(String id) {
        return sysRoleDao.userByRole(id);
    }

    public List<OrganDTO> getChild(String id, List<OrganDTO> rootOrgan) {
        // 子菜单
        List<OrganDTO> childList = new ArrayList<>();

        rootOrgan.forEach((organ) -> {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (id.equals(organ.getParentId())) {
                childList.add(organ);
            }
        });

        // 把子菜单的子菜单再循环一遍

        childList.forEach((organ) -> {
            organ.setChildren(getChild(organ.getId(), rootOrgan));
        });
        // 递归退出条件
        if (childList.size() == 0) {
            return childList;
        }
        return childList;
    }

    @Override
    public boolean isExistRoleName(String id, String name) {
        int count;
        count = sysRoleDao.isExistRoleName(id, name);
        if (count > 0){
            throw new SsoException("该角色名称已经存在，请重新输入");
        }
        return true;
    }
}

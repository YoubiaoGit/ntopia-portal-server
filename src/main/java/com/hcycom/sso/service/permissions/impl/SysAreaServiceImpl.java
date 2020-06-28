package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.cache.SysAreaCache;
import com.hcycom.sso.dao.permissions.SysAreaDao;
import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.exception.SsoException;
import com.hcycom.sso.service.permissions.SysAreaService;
import com.hcycom.sso.utils.ConditionSqlUtil;
import com.hcycom.sso.utils.UUIDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;
@Service
public class SysAreaServiceImpl implements SysAreaService {
    @Autowired
    private SysAreaDao sysAreaDao;
    @Override
//    @Cacheable(value = SysAreaCache.CACHE_ALL_AREA)
    public List<SysArea> findAll(String keywords) {
        List<SysArea> allList = sysAreaDao.findAll(keywords);
        List<SysArea> list = new ArrayList<>();
        if(StringUtils.isEmpty(keywords)) {
            for (SysArea areas: allList) {
                if("0".equals(areas.getParentId())) {
                    list.add(areas);
                }
            }
        } else {
            list = allList;
        }

        if(list.size() > 0) {
            list.forEach((area) -> {
                area.setChildren(getChild(area.getId(), allList));
            });
        }
        return list;
    }

    @Override
//    @Cacheable(value = SysAreaCache.CACHE_AREA_TYPE)
    public List<String> findAreaType() {
        return sysAreaDao.findAreaType();
    }

    @Override
    public SysArea findById(String id) {
        return sysAreaDao.findById(id);
    }

    @Override
    public List<SysArea> findAllTopArea() {
        return sysAreaDao.findAllTopArea();
    }

    @Override
    public List<SysArea> childArea(String code) {
        return sysAreaDao.childArea(code);
    }

    @Override
    @CacheEvict(value = {SysAreaCache.CACHE_ALL_AREA, SysAreaCache.CACHE_AREA_ONE,SysAreaCache.CACHE_AREA_TYPE}, allEntries = true)
    public boolean deleteArea(String id) {
        int i = sysAreaDao.findThisCodeExistPcode(id);
        if(i > 0) {
            //说明该id下的区域存在子区域, 不能删除
            throw  new SsoException("该区域存在子区域,不能随便删除");
        }

        int k = sysAreaDao.deleteByPrimaryKey(id);;
        /*try {
           k = sysAreaDao.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw  new SsoException("该区域关联其他数据, 不能删除");
        }*/
        return k > 0;
    }

    @Override
    @CacheEvict(value = {SysAreaCache.CACHE_ALL_AREA, SysAreaCache.CACHE_AREA_ONE,SysAreaCache.CACHE_AREA_TYPE}, allEntries = true)
    public boolean insertArea(SysArea area) {
        String uuid = UUIDTool.getUUID();
        area.setId(uuid);
        return sysAreaDao.insertSelective(area) > 0;
    }

    @Override
    @CacheEvict(value = {SysAreaCache.CACHE_ALL_AREA, SysAreaCache.CACHE_AREA_ONE,SysAreaCache.CACHE_AREA_TYPE}, allEntries = true)
    public boolean updateArea(SysArea area) {
        return sysAreaDao.updateByPrimaryKeySelective(area) > 0;
    }

    @Override
    public SysArea findByOrgan(String organId) {
        return sysAreaDao.findByOrgan(organId);
    }

    private List<SysArea> getChild(String code, List<SysArea> rootArea) {
        // 子
        List<SysArea> childList = new ArrayList<>();
        //Class<?> a = childList.getClass();
        rootArea.forEach((area) -> {
            // 遍历所有节点，将父id与传过来的id比较
            if (area.getParentId().equals(code)) {
                childList.add(area);
            }
        });
        // 把子再循环一遍
        childList.forEach(area -> {
            area.setChildren(getChild(area.getId(), rootArea));
        });
        // 递归退出条件
        if (childList.size() == 0) {
            return childList;
        }
        return childList;
    }

}

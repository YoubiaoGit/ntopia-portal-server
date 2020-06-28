package com.hcycom.sso.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.dao.BaseFindPageDao;


import java.util.List;


public class BaseFindPageService<T> {
    /**
     * 分页查询公共service
     * @param search
     * @param start
     * @param pageSize
     * @param baseFindPageDao
     * @return
     */
    public PageInfo<T> findByPage(String search, int start, int pageSize, BaseFindPageDao baseFindPageDao) {
        if(start < 1) {start = 1;}
        if(pageSize < 1) {pageSize = 8;}
        Page<T> page = PageHelper.startPage(start, pageSize);
        //从第几条数据开始
        int firstIndex = (start - 1) * pageSize;
        List<T> list = baseFindPageDao.findByPage(search,firstIndex, pageSize);
        long total = baseFindPageDao.total(search);
        page.setTotal(total);
        page.setPageSize(pageSize);
        PageInfo<T> info = page.toPageInfo();
        info.setList(list);
        return info;
    }
}

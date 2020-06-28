package com.hcycom.sso.dao;

import java.util.List;
/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 17:42
 */
public interface BaseFindPageDao<T> {
    /**
     * 分页查询公共dao
     * @param search 模糊搜索关键字
     * @param start
     * @param pageSize
     * @return
     */
    List<T> findByPage(String search, int start, int pageSize);

    /**
     * 总记录数
     * @param search
     * @return
     */
    long total(String search);
}

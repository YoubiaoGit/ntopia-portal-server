package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.domain.SysEms;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysEmsDao {

    /**
     *获取网管
     *@param id
     *@return
     */
    @Select("select id,name,code from sys_ems where 1=1 and id=#{id}")
    SysEms findOne(@Param("id") String id);

    /**
     *获取网管列表
     *@param
     *@return
     */
    @Select("select id,name,code from sys_ems")
    List<SysEms> findAll();
}

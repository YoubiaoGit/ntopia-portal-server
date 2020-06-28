package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.domain.SysSpecialty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface SysSpecialtyDao {

    /**
     *获取专业
     *@param id
     *@return
     */
    @Select("select id,name,code from sys_specialty where 1=1 and id=#{id}")
    SysSpecialty findOne(@Param("id") String id);

    /**
     *获取专业列表
     *@param
     *@return
     */
    @Select("select id,name,code from sys_specialty")
    List<SysSpecialty> findAll();
}

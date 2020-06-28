package com.hcycom.sso.dao;

import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.OrganRelationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserDao {
    @Select("select u.login_name,u.organ_name,u.city_name, u.id, u.full_name, u.telephone, u.email ,  " +
        " u.organ_id,u.pic " +
        " from sys_user u where telephone=#{telephone} and passwd=#{passwd}")
    SysUser findUser(SysUser sysUser);

    @Select("select id, full_name from sys_user where telephone = #{telephone}")
    List<SysUser> findAllUserName(@Param("telephone") String telephone);

    @Select("select id, login_name from sys_user where lower(login_name) = lower(#{loginName})")
    List<SysUser> findUserbyloginName(@Param("loginName") String loginName);

    @Select("select id, telephone,email from sys_user where telephone=#{telephone} or email=#{email}")
    List<SysUser> findPhoneOrEmail(SysUser user);

    @Select("select u.login_name, u.telephone, u.email , u.passwd " +
        " from sys_user u where telephone=#{telephone}")
    SysUser getUser(SysUser sysUser);


    // @MapKey("organ_id")

    @Select("SELECT organ_id as id,count(*) as userCount from sys_user GROUP BY organ_id")
    List<OrganRelationDTO> getUserOrganInfo();

}

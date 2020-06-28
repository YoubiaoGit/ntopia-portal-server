package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.BaseFindPageDao;
import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.SysUserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao extends IMapper<SysUser>, BaseFindPageDao<SysUserDTO>{
    @Select("<script>" +
            "SELECT u.login_name,u.organ_name,u.city_name, u.id, u.full_name, u.telephone, u.email , u.organ_id,u.pic,"
            +"o.sys_area_id ,a.`full_name` as cityName "
            +"FROM ((sys_user u LEFT JOIN sys_organ o ON u.organ_id = o.id) "
            +"left JOIN sys_area a  ON a.id = o.sys_area_id)"
            + "<where>"
            + " u.id Is Not Null "
            +"<if test='search != null'>" +
            "AND (u.full_name like \"%\"#{search}\"%\" or u.organ_name like \"%\"#{search}\"%\" or u.city_name like \"%\"#{search}\"%\" " +
            "  or u.telephone like \"%\"#{search}\"%\"  or u.email like \"%\"#{search}\"%\"  or a.name like \"%\"#{search}\"%\"  )" +
            "</if>"
            + "</where>"+
//            + " limit #{firstIndex} , #{la stIndex}" +
            "</script>")
    @Override
    List<SysUserDTO> findByPage(@Param("search") String search, @Param("firstIndex") int firstIndex, @Param("lastIndex")int lastIndex);


    @Select("<script>" +
            "select count(1) from sys_user u"
            + "<where>"
            +"<if test='search != null'>" +
            "AND ( u.full_name like \"%\"#{search}\"%\" " +
            " or u.telephone like \"%\"#{search}\"%\"  or u.email like \"%\"#{search}\"%\" )" +
            "</if>"
            + "</where>"
            +"</script>")
    @Override
    long total(@Param("search") String search);
    @Delete("delete from sys_user where id=#{id}")
    int deleteUserById(@Param("id") String id);
    @Select("select u.id," +
            "u.`login_name`," +
            "u.organ_id,"+
            "u.passwd," +
            "u.full_name, "+
            "u.telephone," +
            "u.email,u.city_name,u.organ_name,u.pic," +
            "u.create_user from sys_user  u where u.id=#{id}")
    SysUser getById(@Param("id") String id);

    @Select("select u.id," +
            "u.login_name,"+
            "u.full_name," +
            "u.organ_id,"+
            "u.passwd," +
            "u.telephone," +
            "u.email," +
            "u.user_type"+
            "u.pic," +
            "u.freeze_time," +
            "u.freeze_cause," +
            "u.last_login_ip," +
            "u.last_login_time,"+
            "u.update_time," +
            "u.create_user  from sys_user u where u.city_id=#{cityCode}")
//    List<SysUser> getUserByArea(String cityCode);


    @Insert("insert into sys_user  ( `login_name`," +
            "            full_name," +
            "            id," +
            "            organ_id," +
            "            passwd," +
            "            telephone," +
            "            organ_name," +
            "            city_name," +
            "            email," +
            "            pic," +
            "            u.user_type," +
            "            u.freeze_time," +
            "            u.freeze_cause," +
            "            u.last_login_ip," +
            "            u.last_login_time,"+
            "            u.upadte_time," +
            "            create_user ) values(#{loginName},#{fullName},#{id}" +
            "#{organId},#{passwd}," +
            "#{telephone},#{email},#{pic},#{userType},0, #{freezeTime}, #{freezeCause},#{lastLoginIp}, #{lastLoginTime,jdbcType=DATE}, #{updateTime,jdbcType=DATE},#{createUser})")
    int insertUser(SysUser user);
    @Update("update  sys_user u  set u.`login_name`=#{loginName}," +
            "            u.full_name=#{fullName}," +
            "            u.id=#{id}," +
            "            organ_id=#{organId}," +
            "            passwd=#{passwd}," +
            "            telephone=#{telephone}," +
            "            city_name=#{cityName}," +
            "            organ_name=#{organName}," +
            "            email=#{email}," +
            "            pic=#{pic}," +
            "            u.freeze_cause=#{freezeCause}," +
            "            u.last_login_ip=#{lastLoginIp}," +
            "            u.last_login_time=#{lastLoginTime},"+
            "            u.update_time=#{updateTime,jdbcType=DATE}," +
            "            u.create_user=#{createUser}"+
            "              where u.id=#{id}")

    int updateUser(SysUser user);

    //查询机构下的所属人员
    @Select("SELECT u.id, u.`full_name` from sys_user u WHERE u.organ_id in (SELECT o.`id` from sys_organ o)")
    List<SysUser> mainHead();

    //查询密码是否存在
    @Select("select count(1) from sys_user u where u.id= #{id} and u.passwd =#{oldCode}")
    int isPassword(@Param("id") String id,@Param("oldCode") String oldCode);
    //修改用户名密码
    @Update("UPDATE sys_user u SET u.passwd =#{newCode} WHERE u.id=#{id} ")
    int updatePassword(@Param("id") String id,@Param("newCode")String  newCode);
    //根据组织机构查询用户
    @Select("select id from sys_user where organ_id = #{organId}")
    List<String> findUserByOrgan(@Param("organId") String organId);

    @Select("<script>" +
            "SELECT u.login_name,u.organ_name,u.city_name, u.id, u.full_name, u.telephone, u.email , u.organ_id,u.pic,"
            +"o.id,o.sys_area_id ,a.`full_name` as cityName "
            +"FROM ((sys_user u LEFT JOIN sys_organ o ON u.organ_id = o.id) "
            +"left JOIN sys_area a  ON a.id = o.sys_area_id)"
            + "<where>"
            + " u.id Is Not Null "
            +"<if test='search != null'>" +
            "AND (u.full_name like \"%\"#{search}\"%\" or u.organ_name like \"%\"#{search}\"%\" or u.city_name like \"%\"#{search}\"%\" " +
            "  or u.telephone like \"%\"#{search}\"%\"  or u.email like \"%\"#{search}\"%\"  or a.name like \"%\"#{search}\"%\"  )" +
            "</if>"
            + "</where>"
            +"</script>")
    List<SysUserDTO> findAll(@Param("search") String search);

}

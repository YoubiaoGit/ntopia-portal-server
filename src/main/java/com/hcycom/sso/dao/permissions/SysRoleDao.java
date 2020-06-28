package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.BaseFindPageDao;
import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.dto.RoleDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysRoleDao extends IMapper<SysRole>, BaseFindPageDao<RoleDTO> {
/*    @Select("<script>" +
            "select b.id, b.name, count(a.sys_role_id) as userCounts from sys_role b left join sys_user_role a on b.id = a.sys_role_id " +
            "group by a.sys_role_id  "
            + "<where>"
            +"<if test='search != null'>" +
            "AND (r.`name` like \"%\"#{search}\"%\"   or o.name like \"%\"#{search}\"%\"    or r.is_sys like \"%\"#{search}\"%\" )" +
            "</if>"
            + "</where>  order by b.sort "+
//            " limit #{firstIndex}, #{lastIndex} " +
            "</script>")*/
	@Select("<script>" +
            "select b.id, b.name, count(a.sys_role_id) as userCounts from sys_role b left join sys_user_role a on b.id = a.sys_role_id " +
            "<where>"
            +"<if test='search != null'>" +
            "AND (b.`name` like \"%\"#{search}\"%\" )" +
            "</if>"
            + "</where> "+
            "group by b.id  "+
            " order by b.sort "+
//            " limit #{firstIndex}, #{lastIndex} " +
            "</script>")
    @Override
    List<RoleDTO> findByPage(@Param("search") String search, @Param("firstIndex") int firstIndex, @Param("lastIndex")int lastIndex);
    @Select("<script>" +
            "SELECT count(1) FROM sys_role r  "
            + "<where>"
            +"<if test='search != null'>" +
            "AND (r.`name` like \"%\"#{search}\"%\" )" +
            "</if>"
            + "</where>" +
            "</script>")
    @Override
    long total(@Param("search") String search);

    @Select("select id, name from sys_role")
    List<SysRole> findRoleName();
    //根据用户id查询角色信息
    @Select("<script>" +
           "select r.id,r.name from sys_role r"
            +"<where>"
            +" id =any "
            +" (select ur.sys_role_id from sys_user u left join sys_user_role ur on u.id=ur.sys_user_id "
            +"where u.id=#{uid})"
            +" </where>"
            +" </script>")
    List<SysRole> findByUid(String uid);
    //排序
    @Select("select max(sort) from sys_role")
    int getMaxSort();

    @Insert("INSERT INTO sys_role ( id,name,sort,create_user,update_time )" +
            " VALUES( #{id},#{name},#{sort},#{createUser},#{updateTime} ) ")
    int insert(SysRole sysRole);
    //根据角色id获取用户
    @Select("  select full_name from sys_user where id in (\n" +
            " select ur.sys_user_id from sys_role o left join sys_user_role ur on o.id=ur.sys_role_id where o.id= #{id})")
    List<String> userByRole(String id);

    @Select("select sys_menu_id from sys_role_menu where sys_role_id = #{id} ")
	List<String> selectMenuidsByRoleid(String id);

    /**
     * 根据添加或修改时角色名称查询此角色名称是否已经存在
     * @param id
     * @param name
     * @return
     */
    @Select("<script>" +
        "select count(r.name) from sys_role r" +
        "<where>" +
        "r.name = #{name} " +
        "<if test = \"id != null and id != ''\">" +
        "and r.id != #{id}" +
        "</if>" +
        "</where>" +
        "</script>")
    int isExistRoleName(@Param("id") String id, @Param("name") String name);
}

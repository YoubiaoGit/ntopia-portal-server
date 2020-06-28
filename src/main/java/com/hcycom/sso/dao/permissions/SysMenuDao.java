package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysMenu;
import com.hcycom.sso.dto.MenuDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SysMenuDao extends IMapper<SysMenu> {

/*    @Select("<script>" +
            " select  m.id,m.title,m.name ,m.parent_id ,m.icon,m.url,rm.sys_role_id as roleIds  from sys_role_menu rm left join sys_menu m on rm.sys_menu_id = m.id "+
            " <where>" +
            " sys_role_id in  "+
            "<foreach collection=\"roleids\" index = \"index\" item = \"roleid\" open= \"(\" separator=\",\" close=\")\"> "+
            "#{roleid}"+
           " </foreach>"+
            " and m.is_display = 1"+
            " </where>  group by m.id order by m.sort"+
            "</script>")
    List<SysMenu> findParentMenu(@Param("roleids")List<String> roleids);*/
    
    @Select("<script>" +
            " select  m.id,m.title,m.name ,m.parent_id ,m.icon,m.url from sys_role_menu rm left join sys_menu m on rm.sys_menu_id = m.id "+
            " <where>" +
            " sys_role_id in  "+
            "<foreach collection=\"roleids\" index = \"index\" item = \"roleid\" open= \"(\" separator=\",\" close=\")\"> "+
            "#{roleid}"+
           " </foreach>"+
            " and m.is_display = 1"+
            " </where>  group by m.id  order by m.sort"+
            "</script>")
    List<SysMenu> findParentMenu(@Param("roleids")List<String> roleids);

    @Select("SELECT id,title,`name`,url,is_display,icon,parent_id,sort,update_time,create_user from sys_menu t  where parent_id ='1' and is_display = 1 order by sort")
    List<SysMenu> findMenuByPid(String pid);
/*    @Select("<script>" +
            "SELECT id,title,`name`,url,is_display as isDisplay,icon,parent_id  as parentId,sort,update_time as updateTime,create_user as  createUser" +
            "from sys_menu " +
             "<where>" +
            " and  parent_id >= 0 "
            + "</where>   order by sort" +
            "</script>")
    List<SysMenu> findAllMenu();*/
    
    @Select("<script>" +
            "SELECT id,title,`name`,url,is_display,icon,parent_id,sort,update_time as updateTime,create_user, is_lower_menu as isLowerMenu " +
            "from sys_menu " +
             "<where>" +
            " and  parent_id >= 0 "
            + "</where>   order by sort" +
            "</script>")
    List<SysMenu> findAllMenu();

    @Select("SELECT DISTINCT(t.parent_id) as pids FROM sys_menu t")
    List<String> findAllPid();
    @Select("SELECT m.*, (select m1.title from sys_menu m1 where m1.id = m.parent_id) as supername from sys_menu m where m.id not in (SELECT m2.parent_id from sys_menu m2)")
    List<SysMenu> findChildMenu();

    @Select("select m.* from sys_menu m LEFT JOIN sys_role_menu rm on m.id = rm.sys_menu_id where rm.sys_role_id =#{roleid}")
    List<SysMenu> findByRoleId(String roleid);
    //根据id查询父元素
    @Select("select m.*,(select m2.`title` as parentName from sys_menu m2 where m2.id = m.parent_id) as supername, "+
            "(select m3.`url` from sys_menu m3 WHERE m3.id = m.parent_id) AS parentUrl"+
    		" from sys_menu m" +
            "  where m.id=#{id}")
    MenuDTO findByParent(String id);

    //根据子id获取父级
    @Select("<script>" +
            "SELECT T2.id, T2.name,T2.title,T2.parent_id,T2.icon,T2.url,T2.sort                      "+
            "FROM (                                                           "+
            "SELECT                                                           "+
            "@r AS _id,                                                       "+
            "(SELECT @r := parent_id FROM sys_menu WHERE id = _id) AS pid,    "+
            "@l := @l + 1 AS lvl                                              "+
            "FROM                                                             "+
            "(SELECT @r := #{id}, @l := 0) vars,                                "+
            "sys_menu h                                                       "+
            " ) T1                                                "+
            "JOIN sys_menu T2                                                 "+
            "ON T1._id = T2.id   and    T2.is_display=1                               "+
            "</script>")
    List<SysMenu> findParentById(@Param("id")String id);
    @Select("<script>" +
            " select  m.id,m.title,m.name ,m.parent_id ,m.icon,m.url,m.sort,m.is_display,m.create_user,m.update_time,rm.sys_role_id as roleIds  from sys_role_menu rm left join sys_menu m on rm.sys_menu_id = m.id "+
            " <where>" +
            " sys_role_id in  "+
            "<foreach collection=\"roleids\" index = \"index\" item = \"roleid\" open= \"(\" separator=\",\" close=\")\"> "+
            "#{roleid}"+
            " </foreach>"+
            " and m.is_display = 1"+
            " </where>  group by m.id order by m.sort"+
            "</script>")
    List<MenuDTO> menuByRole(@Param("roleids")List<String> roleids);

    @Select("SELECT id FROM sys_menu WHERE parent_id = #{id} ")
    List<String> getChildren(String id);
    
    //更改菜单信息为隐藏
    @Update("UPDATE sys_menu SET is_display = 0 WHERE id = #{id}")
    Boolean hideMenu(String id);
    
    //获取父类id
    @Select("SELECT parent_id FROM sys_menu WHERE id = #{id} ")
    String getParentId(String id );
    
    //更改菜单信息为显示
    @Update("UPDATE sys_menu SET is_display = 1 WHERE id = #{id}")
    Boolean showMenu(String id);
    
    
    //菜单新增路由名称查重
    @Select("SELECT count(*) FROM sys_menu WHERE name = #{param1} and parent_id = #{param2}")
    Boolean checkSaveMenu( String name, String pid);

    //菜单修改路由名称查重
    @Select("SELECT count(*) FROM sys_menu WHERE name = #{param1} AND id <> #{param3} AND parent_id=#{param2}")
    Boolean checkUpdateMenu(String name,String pid,String id);
}

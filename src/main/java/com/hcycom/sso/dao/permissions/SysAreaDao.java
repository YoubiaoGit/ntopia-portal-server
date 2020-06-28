package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysAreaDao extends IMapper<SysArea>{

    @Select("<script>" +
            "select `id`, `name`  ,`code`, type, parent_id, comment ,location,full_name from sys_area " +
            "<where>"
            +"<if test='search != null'>" +
            "AND (`name` like \"%\"#{search}\"%\" or `code` like \"%\"#{search}\"%\"  or   type like \"%\"#{search}\"%\" " +
            " or  parent_id like \"%\"#{search}\"%\"  or   comment like \"%\"#{search}\"%\" )" +
            "</if>"
            + "</where>" +
            "</script>")
    List<SysArea> findAll(@Param("search") String keywords);

    @Select("SELECT DISTINCT(t.type) as types FROM sys_area t")
    List<String> findAreaType();

    /**
     * 查询出当前区域的编码是否存在于父级编码中....(删除方法会用到, 如果存在, 不能删除...)
     * @return
     */
    @Select("SELECT count(a.parent_id) FROM sys_area a WHERE find_in_set((SELECT a1.`code` from sys_area a1 where a1.id = #{id}), a.parent_id)")
    int findThisCodeExistPcode(String id);

    //查询顶级区域
    @Select("select  `id`, `name` ,full_name,`code`, type, parent_id, comment from sys_area where parent_id = '0'")
    List<SysArea> findAllTopArea();

//    @Cacheable("areas")
    @Select("select  `id`, `name` ,full_name, type,location,code,parent_id  from sys_area where parent_id = #{parent_id}")
    List<SysArea> childArea(String parent_id);

    @Select("SELECT\n" +
            "	a.id,\n" +
            "	a.`name`,\n" +
            "	a.`full_name`,\n" +
            "	a.`code`,\n" +
            "	a.type,\n" +
            "	a.comment,\n" +
            "	a.parent_id,\n" +
            "	(\n" +
            "		SELECT\n" +
            "			a2.`name`\n" +
            "		FROM\n" +
            "			sys_area a2\n" +
            "		WHERE\n" +
            "			a2.`id` = a.parent_id\n" +
            "	) AS area\n" +
            "FROM\n" +
            "	sys_area a where a.id=#{id}")
    SysArea findById(String id);

    //根据用户id查区域
    @Select("<script>" +
            "select o.id  as organId, o.sys_area_id ,u.id,u.organ_id, a.id as cityId, a.name,a.full_name " +
            " from sys_organ o ,sys_user u ,sys_area a"
            + "<where>"
            +" o.id = u.organ_id and o.sys_area_id  = a.id "
            +" and u.id = #{uid}"
            + "</where>"
            +"</script>"
    )
    Map<String,Object> areaList(String uid);

    //根据组织机构查区域
    @Select("<script>"
            +"select o.id  as organId, o.sys_area_id , a.id, a.name,a.full_name "
            +" from sys_organ o ,sys_area a"
            + "<where>"
            +" a.id = o.sys_area_id "
            +" and o.id = #{organId}"
            + "</where>"
            +"</script>")
    SysArea findByOrgan(@Param("organId") String organId);
}

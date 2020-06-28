package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysOrgan;
import com.hcycom.sso.dto.CompanyDTO;
import com.hcycom.sso.dto.DepartmentDTO;
import com.hcycom.sso.dto.OrganRelationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysOrganDao extends IMapper<SysOrgan>{

    @Select("<script>" +
            "SELECT  " +
            "o.id,  " +
            "o.`name` as name,  " +
            "o.sort,"+
            "a.`full_name` as areaname,  " +
            "o.type,  " +
            "o.leader,  " +
            "o.sys_area_id , " +
            "o.parent_id , " +
            "o.organ_grade  " +
            " FROM  " +
            "sys_organ o  " +
            "LEFT JOIN sys_area a ON o.sys_area_id = a.id    "
            + "<where>"
            +"<if test='search != null'>" +
            "AND (o.`name` like \"%\"#{search}\"%\" or a.`name` like \"%\"#{search}\"%\"  " +
            " or o.types like \"%\"#{search}\"%\"  or o.note like \"%\"#{search}\"%\" ORDER BY o.sort asc)" +
            "</if>"
            + "</where> order by o.sort" +
            "</script>")
    List<OrganRelationDTO> findAll(@Param("search") String search);
    @Select("select DISTINCT(o.parent_id) as pids FROM sys_organ o")
    List<String> findAllpid();
    //获取所有公司
    @Select("select id, name, parent_id ,type,organ_grade from sys_organ where parent_id = 0 ")
    List<CompanyDTO> findAllCompany();
    //获取机构
    @Select("select  o.id,  o.name,o.parent_id,o.organ_grade,o.sys_area_id,a.full_name as areaName from sys_organ o left join sys_area a on o.sys_area_id = a.id" +
            " where o.id =#{id} ")
    OrganRelationDTO findOrganById(@Param("id")String id);
    //获取所有公司
    @Select("select id, name, parent_id,o.organ_grade, from sys_organ where parent_id =#{parentId} ")
    List<DepartmentDTO> findAllDepartMent(@Param("parentId") String parentId);

    @Select("SELECT\n" +
            "	o.id,\n" +
            "	o.`name`,\n" +
            "	o.sys_area_id,\n" +
            "	a.`name` AS area_name,\n" +
            "   a.parent_id as areaPid,"+
            "	o.type,\n" +
            "	o.leader,\n" +
            "	u.`login_name` AS leaderName,\n" +
            "   o.sort,\n " +
            "   o.organ_grade,\n " +
            "	o.phone,\n" +
            "	o.email,\n" +
            "	o.parent_id \n" +
            "	,(\n" +
            "		SELECT\n" +
            "			o2.`name`\n" +
            "		FROM\n" +
            "			sys_organ o2\n" +
            "		WHERE\n" +
            "			o2.id = o.parent_id\n" +
            "	) AS pname\n" +
            "FROM\n" +
            "	sys_organ o\n" +
            "LEFT JOIN sys_user u ON o.leader = u.id\n" +
            "LEFT JOIN sys_area a ON o.sys_area_id = a.id where o.id=#{id}")
    OrganRelationDTO findById(String id);

    //根据组织机构查询区域
    @Select("select a.id,a.full_name ,o.organ_grade,o.sys_area_id,o.id as organId from sys_organ o ,sys_area a" +
            " where a.id = o.sys_area_id and o.id= #{oid}")
    SysOrgan findAreaByOrgan(String oid);

    //根据id查询所有父机构
    @Select("SELECT T2.id, T2.name,T2.parent_id ,T2.organ_grade,T2.type     "+
                    "FROM (                                                         "+
                    "SELECT                                                         "+
                    "@r AS _id,                                                     "+
                    "(SELECT @r := parent_id FROM sys_organ WHERE id = _id) AS pid, "+
                    "@l := @l + 1 AS lvl                                            "+
                    "FROM                                                           "+
                    "(SELECT @r := #{id}, @l := 0) vars,                              "+
                    "sys_organ h) T1                                                "+
                    "JOIN sys_organ T2                                              "+
                    "ON T1._id = T2.id                                              ")
    List<OrganRelationDTO> findAllParentOrgan(@Param("id")String id);
}

package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.dao.BaseFindPageDao;
import com.hcycom.sso.dao.IMapper;
import com.hcycom.sso.domain.SysFreezeIp;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 17:32
 */
@Mapper
public interface SysFreezeIpDao extends BaseFindPageDao<SysFreezeIp>, IMapper<SysFreezeIp>{

    @Select("<script>" +
            "  SELECT id, ip, start_using, description FROM sys_freeze_ip " +
            "    <where>" +
            "    <if test='search != null'> " +
            "    AND (ip like \"%\"#{search}\"%\" or description like \"%\"#{search}\"%\" ) " +
            "    </if>" +
            "   </where> " +
//            "    limit #{firstIndex}, #{lastIndex}" +
            "   </script>")
    @Override
    List<SysFreezeIp> findByPage(@Param("search") String search, @Param("firstIndex") int firstIndex, @Param("lastIndex")int lastIndex);
    @Select("<script>" +
            "  SELECT count(1) FROM sys_freeze_ip " +
            "    <where>" +
            "    <if test='search != null'> " +
            "    AND (ip like \"%\"#{search}\"%\" or description like \"%\"#{search}\"%\" ) " +
            "    </if>" +
            "   </where> " +
            "   </script>")
    @Override
    long total(@Param("search") String search);
    @Update("update sys_freeze_ip set start_using = #{using} where id=#{id}")
    int updateUsing(@Param("id") String id,@Param("using") String using);
    @Insert("insert into sys_freeze_ip  ( id,`ip`," +
            "            start_using," +
            "            description )"+
            "values(#{id},#{ip},1,#{description})")
    int insertLoginIp(SysFreezeIp sysFreezeIp);

    //根据IP查询
    @Select("SELECT t.id,t.ip,t.start_using from sys_freeze_ip t where t.start_using= 1 and t.ip = #{ip} ")
    SysFreezeIp findByIp(String ip);

    //ip新增查重
    @Select("SELECT count(*) FROM sys_freeze_ip WHERE ip = #{ip}")
    Boolean checkSaveIp(String ip);

    //ip修改查重
    @Select("SELECT count(*) FROM sys_freeze_ip WHERE ip = #{param2} AND id <> #{param1}")
    Boolean checkUpdatIp(String id,String ip);

}

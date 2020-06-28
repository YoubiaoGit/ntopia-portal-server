package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.domain.SysSpecialty;
import com.hcycom.sso.domain.SysSpecialtyOrgan;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author:lr
 * @Date:Created
 */
@Mapper
public interface SysSpecialtyOrganDao {
    @Select("SELECT  *  FROM  sys_specialty_organ")
    List<SysSpecialtyOrgan> select();
    @Insert(" INSERT INTO sys_specialty_organ (specialty_id,organ_id) VALUES (#{specialtyId},#{organId})")
    int insert(SysSpecialtyOrgan sysSpecialtyOrgan);
    @Update(" update sys_specialty_organ  set specialty_id =#{specialtyId} where organ_id= #{organId} ")
    int update(SysSpecialtyOrgan sysSpecialtyOrgan);
    @Delete("DELETE  sys_specialty_organ  WHERE organ_id = #{organId}")
    int delete(@Param("organId") String organId);
    
    @Select("SELECT full_name from sys_user WHERE organ_id = #{id}")
    List<String> findUsersByOrganId(String id);

}

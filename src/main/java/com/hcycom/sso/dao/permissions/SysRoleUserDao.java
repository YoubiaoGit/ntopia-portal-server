package com.hcycom.sso.dao.permissions;

import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-05-07 9:47
 */
@Mapper
public interface SysRoleUserDao {
    final static Logger logger = LoggerFactory.getLogger(SysRoleUserDao.class);
    @InsertProvider(type = RoleUserProvider.class, method = "insertAll")
    int insertBatch(@Param("list") List<String> list);
    @Delete("delete from sys_user_role where sys_user_id=#{id}")
    int deleteByUserid(String id);
    @Select("select DISTINCT(sys_role_id) from sys_user_role where sys_user_id=#{id}")
    List<String> findByRoleid(String id);

    class RoleUserProvider {
        public String insertAll (Map map) {
            List<String> list = (List<String>) map.get("list");
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO sys_user_role ");
            sb.append("(sys_user_id, sys_role_id) ");
            sb.append("VALUES ");
            for (int i = 1; i < list.size(); i++) {
                sb.append( " ( \""+list.get(0)+"\", \""+list.get(i)+"\") ");
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            logger.info("insert sys_user_role  sql -->  "+sb.toString());
            return sb.toString();
        }


    }
}

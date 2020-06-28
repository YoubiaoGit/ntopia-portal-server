package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.domain.SysRoleMenu;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-20 11:56
 * 角色和菜单关系表
 */
@Mapper
public interface SysRoleMenuDao {
    final static Logger logger = LoggerFactory.getLogger(SysRoleMenuDao.class);
    @InsertProvider(type = RoleMenuProvider.class, method = "insertAll")
    int insertBatch(@Param("list") List<String> list);
    @Delete("delete from  sys_role_menu where sys_role_id = #{id}")
    int deleteByRoleid(String id);

    @Select("select role_id, sys_menu_id from sys_role_menu where sys_role_id = #{id}")
    List <SysRoleMenu> findMenuIdByRoleid(String id);

    class RoleMenuProvider {
        public String insertAll (Map map) {
            List<String> list = (List<String>) map.get("list");
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO sys_role_menu ");
            sb.append("(sys_role_id, sys_menu_id) ");
            sb.append("VALUES ");
           // MessageFormat mf = new MessageFormat("(#{list[{0}]}, #{list[{1}]})");
            for (int i = 1; i < list.size(); i++) {
                sb.append( " ( \""+list.get(0)+"\", \""+list.get(i)+"\") ");
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            logger.info("SysRoleMenuDao sql --> "+sb.toString());
            return sb.toString();
        }
    }

}

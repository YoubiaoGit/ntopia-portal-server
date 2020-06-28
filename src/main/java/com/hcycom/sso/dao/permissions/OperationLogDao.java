package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.domain.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:
 * @Date:Created
 */
@Mapper
public interface OperationLogDao {
    @Select(
    ("<script>" +
            " select sol.id,sol.type,sol.title,sol.oper_time,sol.biz_key,sol.remote_addr,sol.server_addr,su.full_name userName,su.login_name as loginName\n"+
            "        from sys_operation_log sol LEFT JOIN sys_user su ON sol.sys_user_id=su.id\n"+
            "        <where>\n"+
            "            1=1\n"+
            "            <if test=\"startTime!=null and startTime!='' and endTime!=null and endTime!='' \">\n" +
            "                and (\n" +
            "                  oper_time &gt;= #{startTime} AND oper_time&lt;= #{endTime}\n" +
            "                )\n" +
            "            </if>\n" +
            "            <if test=\"keyWords!=null and keyWords.size()>0\">\n"+
            "              <foreach collection=\"keyWords\"  item=\"keyWord\" index=\"index\" >\n"+
            "                and (  sol.title like binary concat('%',#{keyWord},'%')\n"+
            "                OR sol.oper_time like binary concat('%',#{keyWord},'%')\n"+
            "                OR sol.remote_addr like binary concat('%',#{keyWord},'%')\n"+
            "                OR sol.server_addr like binary concat('%',#{keyWord},'%')\n"+
            "                OR su.full_name like binary concat('%',#{keyWord},'%')\n"+
            "                OR su.login_name like binary concat('%',#{keyWord},'%')\n"+
            "                  )\n"+
            "              </foreach>\n"+
            "            </if>\n"+
            "        </where>\n"+
            "        ORDER BY sol.oper_time DESC"+
            "</script>")
            )
    List<OperationLog> findAll(@Param("keyWords") List<String> keyWords, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /*@Select("<select id=\"findOne\" resultType=\"com.hcycom.mc.monitor.api.domain.OperationLog\">\n" +
        "        SELECT a.* ,b.full_name userName,b.telephone as loginName\n" +
        "        FROM\n" +
        "          (select * from sys_operation_log  where id=#{id}) a\n" +
        "          LEFT JOIN sys_user b ON a.sys_user_id=b.id\n" +
        "    </select>")
    OperationLog findOne(String id);*/

    @Select("<script>" +
        "select a.*,b.full_name as userName,b.login_name as loginName from " +
        "(select * from sys_operation_log where id = #{id}) as a " +
        "left join sys_user b on a.sys_user_id = b.id" +
        "</script>")
    OperationLog findOne(String id);
}

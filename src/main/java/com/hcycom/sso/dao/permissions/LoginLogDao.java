package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.domain.LoginLog;
import com.hcycom.sso.dto.LoginLogCountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:lr
 * @Date:Created
 */
@Mapper
public interface LoginLogDao {

    @Select("<script>" +
        " select l.id,l.ip_addr,l.start_time,l.end_time,l.ip_location,l.isp,l.create_user,l.update_time,l.sys_user_id,u.login_name as loginName\n" +
        "        from sys_login_log l LEFT JOIN sys_user u on u.id = l.sys_user_id\n" +
        "        <where>\n" +
        "            <if test=\"startTime!=null and startTime!='' and endTime!=null and endTime!='' \">\n" +
        "                (\n" +
        "                  (start_time &gt;= #{startTime} AND end_time&lt;= #{endTime})\n" +
        "                  OR\n" +
        "                  (start_time &gt;= #{startTime} AND start_time &lt;= #{endTime}  AND end_time is null)\n" +
        "                )\n" +
        "            </if>\n" +
        "            <if test=\"keyWords!=null and keyWords.size()>0\">\n" +
        "                <foreach collection=\"keyWords\"  item=\"keyWord\" index=\"index\">\n" +
        "                   AND (l.ip_addr like binary concat('%',#{keyWord},'%')\n" +
        "                    OR  l.start_time like binary concat('%',#{keyWord},'%')\n" +
        "                    OR l.end_time like binary concat('%',#{keyWord},'%')\n" +
        "                    OR  l.ip_location like binary concat('%',#{keyWord},'%')\n" +
        "                    OR  l.isp like binary concat('%',#{keyWord},'%')\n" +
        "                    OR l.create_user like binary concat('%',#{keyWord},'%')\n" +
        "                    OR l.update_time like binary concat('%',#{keyWord},'%')\n" +
        "                    OR u.login_name like binary concat('%',#{keyWord},'%')\n" +
        "                    )\n" +
        "                </foreach>\n" +
        "            </if>\n" +
        "        </where>\n" +
        "        ORDER BY l.start_time DESC" +
        "</script>")
    List<LoginLog> findAll(@Param("keyWords") List<String> keyWords, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select(" select id,ip_addr,start_time,end_time,ip_location,isp,create_user,update_time,sys_user_id from sys_login_log " +
        "        where" +
        "         id=#{id}")
    LoginLog findOne(@Param("id") String id);

    @Select(
        " SELECT t1.today as todayCount,\n" +
            "          (case when t1.yesterday=0 then  1 else ROUND((t1.today - t1.yesterday) / t1.yesterday , 2) end) as dayRatio,\n" +
            "          (case when t1.lastwk=0 then 1 else ROUND((t1.wk - t1.lastwk) / t1.wk , 2) end) as weekRatio\n" +
            "\t\t  FROM\n" +
            "            (SELECT  sum(t.today) as today,\n" +
            "              sum(t.yesterday) as yesterday,\n" +
            "              sum(t.wk) as wk,\n" +
            "\t\t\t  sum(t.lastwk) as lastwk\n" +
            "              from (SELECT\n" +
            "                      case when to_days(start_time) >= to_days(#{startTime}) and to_days(start_time) <= to_days(#{endTime})  then 1 else 0 end as today,\n" +
            "                      case when date_sub(curdate(), INTERVAL 1 DAY) = date(start_time) then 1 else 0 end as yesterday,\n" +
            "                      case when YEARWEEK(date_format(start_time,'%Y-%m-%d')) = YEARWEEK(now()) then 1 else 0 end as wk,\n" +
            "                      case when YEARWEEK(date_format(start_time,'%Y-%m-%d')) = YEARWEEK(date_sub(now(), INTERVAL 1 YEAR)) then 1 else 0 end as lastwk\n" +
            "                      from sys_login_log\n" +
            "                    )t\n" +
            "\t\t\t)t1"
    )
    LoginLogCountDto queryCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select(" SELECT count(l.id) `count`,a.id city FROM sys_login_log l,sys_user u,sys_organ o,sys_area a\n" +
        "        WHERE l.sys_user_id=u.id AND u.organ_id = o.id AND o.sys_area_id = a.id\n" +
        "        AND l.start_time >=#{startTime}\n" +
        "        AND l.start_time <=#{endTime}\n" +
        "        GROUP BY a.id")
    List<LoginLogCountDto> queryCountByCity(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("<script> " +
        "        select id,start_time from\n" +
        "        sys_login_log\n" +
        "        <where>\n" +
        "            <if test=\"list!=null and list.size()>0\">\n" +
        "                AND id IN\n" +
        "                <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" open=\"(\" close=\")\">\n" +
        "                    #{item}\n" +
        "                </foreach>\n" +
        "            </if>\n" +
        "        </where>\n" +
        " </script>")
    List<LoginLog> findLoginTimeBatch(@Param("list") List<String> list);


}

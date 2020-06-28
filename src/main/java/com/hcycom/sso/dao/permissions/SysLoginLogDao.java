package com.hcycom.sso.dao.permissions;

import com.hcycom.sso.domain.SysLoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * @Author:lirong
 * @Date:Created
 * 登陆日志
 */
@Mapper
public interface SysLoginLogDao {
    @Insert(("insert into sys_login_log  ( id,`ip_addr`,ip_location,isp," +
            " start_time,end_time,create_user,sys_user_id )" +
            " values(#{id},#{ipAddr},#{ipLocation},#{isp},#{startTime},#{endTime},#{createUser},#{sysUserId})"))
    int insertLog(SysLoginLog sysLoginLog);
    @Update("update sys_login_log set end_time = #{endTime} where id=#{id}")
    int update(@Param("id") String id,@Param("endTime") Date endTime);
    @Update("update sys_login_log set end_time = #{endTime} where sys_user_id=#{uid} and `ip_addr`=#{ip} and ISNULL(end_time)")
    int updatebyIp(@Param("ip") String ip,@Param("uid") String uid,@Param("endTime") Date endTime);
   //更新失效的退出时间
    @Update(" update sys_login_log set end_time = DATE_ADD(start_time,INTERVAL 30 MINUTE)\n" +
            "    where DATE_ADD(start_time,INTERVAL 30 MINUTE) < NOW() AND end_time is NULL")
    int updateLoginOutDate();


}

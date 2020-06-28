package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author:lirong
 * @Date:Created
 * 登录日志
 */
@Getter
@Setter
@ToString
public class SysLoginLog {

    private String id ;
    private String ipAddr;//ip地址
    private Date startTime;//登陆时间
    private Date endTime;//登出时间
    private String ipLocation;//ip地址
    private String isp;//isp运营商
    private Date updateTime;//修改时间
    private String createUser;//创建人
    private String sysUserId;

}

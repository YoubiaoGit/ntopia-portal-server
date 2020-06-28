package com.hcycom.sso.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: LoginLog
 * @Description: TODO
 * @auther: lr
 * @date: 2018/6/27 21:28
 * @version: 1.0
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginLog implements Serializable {

    // id
    private String id;

    // IP地址
    private String ipAddr;

    // 登陆时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    // 登出时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    // IP归属地
    private String ipLocation;

    // ISP运营商
    private String isp;

    // 创建人
    private String createUser;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    // 系统用户id
    private String sysUserId;

    //登录账号
    private  String  loginName;
}


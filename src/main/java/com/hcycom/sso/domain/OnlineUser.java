package com.hcycom.sso.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: OnlineUser
 * @Description: TODO
 * @auther: lr
 * @date: 2018/3/19 21:56
 * @version: 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class
OnlineUser implements Serializable {

    /** 用户ID **/
    private String id;

    /** 登录名 **/
    private String loginName;

    /** 用户姓名 **/
    private String fullName;

    /** 手机号码 **/
    private String telephone;

    /** 邮箱 **/
    private String email;

    /** 头像 **/
    private String pic;

    /** 机构id **/
    private String organId;

    /** 专业 **/
    private String specialty;

    /** 城市id **/
    private String cityId;

    /** 城市名称 **/
    private String cityName;

    /** 公司名称 **/
    private String company ;

    /** 用户角色**/
    private List<String> roleids;

    // 登陆时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginTime;

    /** 在线时长 **/
    private String onlineTime ;

    @Override
    public String toString (){
        return loginName+fullName+email+company+cityName;
    }

}


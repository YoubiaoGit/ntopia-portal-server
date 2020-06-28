package com.hcycom.sso.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hcycom.sso.domain.SysRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.util.List;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-23 13:20
 */
@Getter
@Setter
@ToString
public class SysUserDTO {
    private String id;
    private String loginName; //登录名称
    private String cityName;//城市名称
    private String organName;//组织机构id
    private String fullName;
    private String telephone;//手机号
    private String email;//邮箱
    private String cityId;
    private String organId;//所属组织机构
    private String pic;//头像
    //@Transient
    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    private String[] roleids;

    @Transient
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private String token;//token
    @Transient
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private Integer expire;//存活时间
    //@Transient
    private String organGrade; //机构等级
    //@Transient
    private String company ; //公司名称
    //@Transient
    private String specialty; //所属专业
   // @Transient
    private List<MenuDTO> sysMenuList;//用户所属菜单
    
    private List<SysRole>  sysRoles;
}

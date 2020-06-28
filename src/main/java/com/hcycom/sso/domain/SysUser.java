package com.hcycom.sso.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Getter
@Setter
@ToString
public class SysUser implements Serializable {
    @Id
    private String id;//主键ID
    private String loginName; //登录名称
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String passwd;//密码
    private String fullName;//全名
    @NotNull(message = "电话不能为空")
    private String telephone;//手机号
    @NotNull(message = "邮箱不能为空")
    private String email;//邮箱
    @NotNull(message = "所属组织机构不能为空")
    private String organId;//所属组织机构
    private String cityName;//城市名称
    private String organName;//组织机构id
    private String pic;//头像
    private String freezeTime;//冻结时间(默认为1)
    private String freezeCause;//冻结原因
    private String lastLoginIp; //最后登陆ip
    private Date lastLoginTime;  //最后登陆时间
    private String createUser; //创建人
    private Date updateTime;//修改时间


    //@Transient
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<String> roleids;

    @Transient
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String token;//token
    @Transient
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer expire;//存活时间


    @Transient
    private String cityId;//城市Id
}

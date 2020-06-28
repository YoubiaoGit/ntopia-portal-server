package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 登录IP
 */
@Getter
@Setter
@ToString
public class SysFreezeIp extends BaseDomain{
    private String ip;
    private Integer startUsing;//是否启用(0启用(默认) -1禁用)
    private String description;//描述
    private String createUser;//创建人
    private Date updateTime;//修改时间
}

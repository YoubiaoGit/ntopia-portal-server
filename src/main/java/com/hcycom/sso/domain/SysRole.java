package com.hcycom.sso.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@Getter
@Setter
@ToString
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotNull(message = "角色名称不能为空")
    private String name;//名称
    private String isSys; //系统内置（1是 0否）
    private int sort;//排序
    private String createUser;//创建人
    private Date updateTime;//修改时间


    private List<String> menuids;
    @Transient
    private Boolean checked = false;
}

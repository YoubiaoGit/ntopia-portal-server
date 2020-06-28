package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 区域
 */

@Getter
@Setter
@ToString
public class SysArea extends BaseDomain{

    private String name;
    private String parentId;
    private String code;
    private String type;
    private String comment;
    private String fullName;
    private String location;
    private String createUser;//创建人
    private Date updateTime;//修改时间


    //@Transient
    private String area;//父级名称
    //@Transient
    private List<SysArea> children = new ArrayList<>(0);

}

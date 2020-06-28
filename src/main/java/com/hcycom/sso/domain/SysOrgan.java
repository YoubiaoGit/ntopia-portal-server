package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class SysOrgan extends BaseDomain{
    @NotNull(message = "机构名称不能为空")
    private String name;//机构名称
//    @NotNull(message = "所属区域不能为空")
    private String sysAreaId;//归属区域(区域id)
    @NotNull(message = "机构类型不能为空")
    private String type;//机构类型(1集团公司，2省公司，3市公司）
    @NotNull(message = "上级区域不能为空")
    private String parentId;//上级ID
    @NotNull(message = "机构级别不能为空")
    private String organGrade;//机构级别(一级/二级/三级)
    private String leader;
    @NotNull(message = "联系方式不能为空")
    private String phone;//联系方式
    @NotNull(message = "邮箱不能为空")
    private String email;//邮箱
    private String createUser; //创建人
    private Date updateTime;//修改时间
    private Integer sort;//排序(默认100)

}

package com.hcycom.sso.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * organ机构中外键相关联的对象
 * @Author: LiuGenshi
 * @Date:Created in 2018-05-10 11:25
 */
@Getter
@Setter
@ToString
public class OrganRelationDTO extends SuperOrgan{
    private String id;
    private String name;//机构名称
    private String sysAreaId;//归属区域(区域id)
    private String areaName;//区域名称
    private String code;//机构编码
    private String type;//机构类型(公司/部门)
    private String organGrade;//机构级别(一级/二级/三级)
    private String leader;//主负责人(用户id)
    private String leaderName;//主负责人名称
    private String phone;//联系方式
    private String email;//邮箱
    private String parentId;//上级ID
    private String pname;//上级名称
    private Date updateTime;
    private Integer sort;//排序(默认100)
    @Transient
    private String areaPid;//区域父id
    @Transient
    private Boolean checked = false;
    
    private int userCount; //属于该机构的用户数量
}

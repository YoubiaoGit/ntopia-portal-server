package com.hcycom.sso.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hcycom.sso.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 机构查询封装(用于分页显示)
 */
@Setter
@Getter
@ToString
public class OrganDTO extends SuperOrgan{
 private String areaname;//所属区域
 private BigDecimal sort ; //排序
 private String type;//机构类型(公司/部门)
 private String leader;//负责人
 private String sysAreaId;//所属区域

 @Transient
 @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
 private String search;//模糊查询条件
}

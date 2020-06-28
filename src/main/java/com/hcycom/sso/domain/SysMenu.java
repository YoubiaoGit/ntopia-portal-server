package com.hcycom.sso.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class SysMenu {
    @Id
    private String id;
    @NotNull(message = "菜单名称不能为空")
    private String name;
    @NotNull(message = "标题不能为空")
    private String title;
    private String icon;
    @NotNull(message = "路径不能为空")
    private String url;//动态路由地址
    private String parentId; //父元素
    @NotNull(message = "排序数字不能为空")
    private Integer sort;
    private String isDisplay;//是否显示（1显示 0隐藏）
    private String createUser; //创建人
    private Date updateTime;//修改时间
    @Transient
    private Boolean checked = false;
    @Transient
    private String supername;
    @Transient
    private String displayName;
    
    //新增字段是否存在下级子菜单(1为有下级子菜单，0位没有下级子菜单)
    private String isLowerMenu;  

    private List<SysMenu> children = new ArrayList<>(0);

}

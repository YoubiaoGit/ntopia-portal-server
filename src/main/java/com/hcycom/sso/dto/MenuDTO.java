package com.hcycom.sso.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-22 16:56
 */

@Setter
@Getter
@ToString
public class MenuDTO {
    private String id;
    private String title;
    private String name;
    private String icon;
    private String url;//动态路由地址
    private String parentId; //父元素
    private Integer sort;
    private String isDisplay;//是否显示（1显示 0隐藏）
    private String createUser; //创建人
    private Date updateTime;//修改时间
    
    //新增字段是否存在下级子菜单(1为有下级子菜单，0位没有下级子菜单)
    private String isLowerMenu;  
    
    private List<MenuDTO> children = new ArrayList<>(0);
    private String supername;
    
    //新增字段父类url
    private String parentUrl;

}

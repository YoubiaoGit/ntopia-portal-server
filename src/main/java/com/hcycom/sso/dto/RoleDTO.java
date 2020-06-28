package com.hcycom.sso.dto;

import com.hcycom.sso.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.util.Date;

/**
 * 角色数据封装
 */

@Setter
@Getter
@ToString
public class RoleDTO extends BaseDomain{

    private String name;//名称
    private String isSys; //系统内置（1是 0否）
    private String createUser;//创建人
    private String updateTime; //修改时间
    private String organName;// 所属机构
    private String userCounts;//角色下用户数量



    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }


}

package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;

@Getter
@Setter
@ToString
public class SysRoleMenu {
    @Id
    private String id;
    private String roleId;
    private String menuId;
}

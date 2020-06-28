package com.hcycom.sso.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SysRoleUser {
    private Integer id;
    private Integer userId;
    private Integer roleId;
}

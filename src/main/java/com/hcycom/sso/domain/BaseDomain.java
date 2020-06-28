package com.hcycom.sso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Transient;
@Getter
@Setter
public class BaseDomain {
    @Id
    protected String id;
    @JsonIgnore
    @Transient
    protected String search;//模糊查询条件

}

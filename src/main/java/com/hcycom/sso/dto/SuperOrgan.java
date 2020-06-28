package com.hcycom.sso.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 查询公司或部门的公共对象
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-27 14:37
 */

@Setter
@Getter
@ToString
public class SuperOrgan {
    @Id
    protected String id;
    protected String name;//名称
    protected String parentId;//上级机构id
    protected List<? extends SuperOrgan> children = new ArrayList<>(0);
}

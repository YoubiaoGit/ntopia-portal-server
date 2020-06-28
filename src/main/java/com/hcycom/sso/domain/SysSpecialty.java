package com.hcycom.sso.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: lirong
 * @Date:Created 2018/06/28
 */
@Data
public class SysSpecialty {

    private String id;
    private String name; //专业名称
    private String code; //专业编码


}

package com.hcycom.sso.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: LoginLogCountDto
 * @Description: TODO
 * @auther: lr
 * @date: 2018/6/29 9:04
 * @version: 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginLogCountDto implements Serializable {
    // 今日登陆次数
    private Integer todayCount;

    // 日环比
    private BigDecimal dayRatio;

    //周同比
    private BigDecimal weekRatio;

    // 登陆次数
    private Integer count;

    // 城市
    private String city;
}


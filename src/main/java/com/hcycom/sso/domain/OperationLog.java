package com.hcycom.sso.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: OperationLog
 * @Description: TODO
 * @auther: lr
 * @date: 2018/6/27 20:41
 * @version: 1.0
 */
@Data
public class OperationLog implements Serializable {

    // id
    private String id;

    // 日志类型
    private String type;

    // 日志标题
    private String title;

    // 操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date operTime;

    // 业务主键
    private String bizKey;

    // 操作地址
    private String remoteAddr;

    // 请求服务器地址
    private String serverAddr;

    // 系统用户id
    private String sysUserId;

    private String userName;//用户姓名

    private String loginName;//用户登录账号

}

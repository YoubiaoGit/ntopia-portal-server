package com.hcycom.sso.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Path：
 * @Classname：
 * @Description：
 * @Author：yandi
 * @CreateTime：2018/8/28 13:22
 * @ModifyUser：yandi
 * @ModifyRemark：
 * @ModifyTime：2018/8/28 13:22
 */
@Data
public class RequestVO implements Serializable{
    private String ip;
    private String telephone;
    private String passwd;
    private String token;
    private String requestURI;
    private String jsonpCallback;
}


package com.hcycom.sso.vo;

import lombok.ToString;

@ToString
public class TokenVO {
    private String token;//token
    private int expire;//存活时间

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}

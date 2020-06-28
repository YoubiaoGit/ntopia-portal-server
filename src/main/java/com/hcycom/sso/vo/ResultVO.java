package com.hcycom.sso.vo;

import lombok.Data;

/**
 * http返回对象
 */
@Data
public class ResultVO<T> {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 返回内容
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

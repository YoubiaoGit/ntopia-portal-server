package com.hcycom.sso.exception;

import com.hcycom.sso.vo.ResultVO;

public class SsoException extends RuntimeException{
    private ResultVO result;

    public SsoException(ResultVO result) {
        super(result.getMessage());
        this.result = result;
    }
    public SsoException(String msg) {
        super(msg);
    }
    public ResultVO getResult() {
        return result;
    }
}

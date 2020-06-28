package com.hcycom.sso.utils;


import com.hcycom.sso.vo.ResultVO;

/**
 * http请求返回工具类
 */
public class ResultVOUtil {

    /**
     * 成功-有参数
     * @param object
     * @return
     */
    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        resultVO.setData(object);
        return  resultVO;
    }

    /**
     * 成功-无参数
     * @return
     */
    public static ResultVO success(){
        return success(null);
    }

    public static ResultVO error(Integer code,String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(msg);
        return resultVO;
    }
}

package com.hcycom.sso.service;

import com.hcycom.sso.client.AuthorizedFeignClient;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.OrganRelationDTO;
import com.hcycom.sso.dto.SysUserDTO;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.TokenVO;
import com.hcycom.sso.vo.UserInfoVO;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface SysUserService {

    SysUser findUser(String username, String passwd);


    UserInfoVO getByToken(HttpServletRequest request, HttpServletResponse response, String token);

    UserInfoVO getLoginUser(HttpServletRequest request, HttpServletResponse response) ;

    ResultVO<SysUser> checkUser(HttpServletRequest request, HttpServletResponse response);
    //获取cookie中的值
    String getCookieValue(HttpServletRequest request,String cookieName);

    //获取cookie下的token
    String getToken(HttpServletRequest request);
    //清理全局cookie(token)
    void clearCookies(HttpServletRequest request, HttpServletResponse response);

    UserInfoVO getRedisUser();

    SysUser getUser(String telephone);
    
    //获取机构的用户信息 Map<机构id，数据该机构的用户信息>
    List<OrganRelationDTO> getUserOrganInfo();
}

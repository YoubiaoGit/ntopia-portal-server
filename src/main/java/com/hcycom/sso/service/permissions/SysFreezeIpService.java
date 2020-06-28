package com.hcycom.sso.service.permissions;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.domain.SysFreezeIp;
import com.hcycom.sso.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 17:44
 */
public interface SysFreezeIpService {
    PageInfo<SysFreezeIp> findByPage(String search, int pageNum, int pageSize);
    boolean updateUsing(String id, String using);
    boolean update(SysFreezeIp sysFreezeIp);
    boolean delete(String id);
    boolean save(SysFreezeIp sysFreezeIp);
    SysFreezeIp findBy(String ip);
    ResultVO<String> checkIp(String id ,String ip);
}

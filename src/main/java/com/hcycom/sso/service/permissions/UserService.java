package com.hcycom.sso.service.permissions;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.SysUserDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {

    PageInfo<SysUserDTO> findByPage(String search, int currentPage, int pageSize);

    boolean deleteById(String id);

    SysUser getById(String id);

    //    List<SysUser> getUserByArea(String cityCode);
    boolean insertUser(SysUser user);

    boolean updateUser(SysUser user);

    JSONObject uploadImg(String path, MultipartFile file, HttpServletRequest request);

    //查询机构下的所属人员
    List<Map<String, Object>> mainHead();

    //查询密码是否存在
    boolean isPassword(String id, String oldCode);

    //修改用户名密码
    boolean updatePassword(String id, String oldCode, String newCode);

    //根据组织机构查询用户
    List<String> findUserByOrgan(String organId);

    List<SysUserDTO> findAll(String search);

}

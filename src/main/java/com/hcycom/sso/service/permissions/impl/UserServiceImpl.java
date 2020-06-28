package com.hcycom.sso.service.permissions.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.cache.SysUserCache;
import com.hcycom.sso.dao.permissions.SysAreaDao;
import com.hcycom.sso.dao.permissions.SysOrganDao;
import com.hcycom.sso.dao.permissions.SysRoleDao;
import com.hcycom.sso.dao.permissions.SysRoleUserDao;
import com.hcycom.sso.dao.permissions.UserDao;
import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.domain.User;
import com.hcycom.sso.dto.OrganRelationDTO;
import com.hcycom.sso.dto.SysUserDTO;
import com.hcycom.sso.dto.UserDTO;
import com.hcycom.sso.dto.UserDTO2;
import com.hcycom.sso.exception.SsoException;
import com.hcycom.sso.service.BaseFindPageService;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.UaaFeignClient;
import com.hcycom.sso.service.permissions.UserService;
import com.hcycom.sso.utils.CommonUtil;
import com.hcycom.sso.utils.FileUtils;
import com.hcycom.sso.utils.UUIDTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class UserServiceImpl extends BaseFindPageService<SysUserDTO> implements UserService {
    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private SysRoleUserDao sysRoleUserDao;

    @Autowired
    private UaaFeignClient feignClient;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysAreaDao sysAreaDao;
    @Autowired
    private SysOrganDao sysOrganDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
//    @Cacheable(value = SysUserCache.CACHE_FIND_PAGE)
    public PageInfo<SysUserDTO> findByPage(String search, int currentPage, int pageSize) {
        PageInfo<SysUserDTO> info = findByPage(search, currentPage, pageSize, userDao);
        List<SysUserDTO> sysUserDTOList = info.getList();
        //多个组织机构
        for (SysUserDTO sysUserDTO : sysUserDTOList) {
            String organId = sysUserDTO.getOrganId();//获取用户所属机构
            OrganRelationDTO organ = sysOrganDao.findById(organId);
            if (organ != null) {
                //获取公司名称
                List<OrganRelationDTO> allOrgan = sysOrganDao.findAllParentOrgan(organId);
                String organGrade = organ.getOrganGrade();
                for (OrganRelationDTO organRelationDTO : allOrgan) {
                    if (organRelationDTO.getType().equals("公司") && organRelationDTO.getOrganGrade().equals(organGrade)) {
                        String company = organRelationDTO.getName();
                        sysUserDTO.setCompany(company);
                        break;
                    }

                }
            }
            SysArea sysArea = sysAreaDao.findByOrgan(organId);
            if (sysArea != null) {
                sysUserDTO.setCityName(sysArea.getFullName());
            }

            List<SysRole> list = sysRoleDao.findByUid(sysUserDTO.getId());
            if (list != null && list.size() > 0) {
                sysUserDTO.setSysRoles(list);
            }
        }
        return info;
    }

    @CacheEvict(value = SysUserCache.CACHE_FIND_PAGE, allEntries = true)
    public boolean deleteById(String id) {
        SysUser sysUser = userDao.getById(id);
        if (sysUser != null && sysUser.getLoginName() != null) {
            //删除uaa中的用户信息
            ResponseEntity<Void> responseEntity = feignClient.deleteUser(sysUser.getLoginName());
            logger.debug("删除用户返回状态码：" + responseEntity.getStatusCode());
            int i = userDao.deleteUserById(id);
            // 在删除用户的同时删除用户角色中间表中的数据
            sysRoleUserDao.deleteByUserid(sysUser.getId());
            /*try {
                i = userDao.deleteUserById(id);
            } catch (Exception e) {
                throw  new SsoException("该用户管理其他数据, 不能删除");
            }*/
            return i > 0;
        }
        return false;
    }

    public SysUser getById(String id) {
        SysUser user = userDao.getById(id);
        user.setToken(null);
        user.setExpire(null);
        return user;
    }

//    @Override
//    public List<SysUser> getUserByArea(String cityCode) {
//        return userDao.getUserByArea(cityCode);
//    }


    @Override
    public List<Map<String, Object>> mainHead() {
        List<SysUser> list = userDao.mainHead();
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        for (SysUser sysUser : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", sysUser.getId());
            map.put("label", sysUser.getLoginName());
            newList.add(map);
        }
        return newList;
    }

    //判断用户密码是否存在
    public boolean isPassword(String id, String oldCode) {
        int count = userDao.isPassword(id, oldCode);
        return count > 0;
    }

    //修改用户名密码
    public boolean updatePassword(String id, String oldCode, String newCode) {
        boolean isUpdateMySelf = false;
        boolean isPass = isPassword(id, oldCode);
        int i = userDao.updatePassword(id, newCode);
        if (isPass == false) {
            throw new SsoException("原密码错误！");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (!StringUtils.isEmpty(newCode)) {
            //如果修改了密码，则清空cookie,重新登录
            logger.info("修改了自己，需要重新登录");
            sysUserService.clearCookies(request, response);
            isUpdateMySelf = true;
        }
        return i > 0;
    }

    //根据组织机构获取用户
    @Override
    public List<String> findUserByOrgan(String organId) {
        return userDao.findUserByOrgan(organId);
    }

    @Override
    public List<SysUserDTO> findAll(String search) {
        List<SysUserDTO> sysUserDTOList = userDao.findAll(search);

        //多个组织机构
        for (SysUserDTO sysUserDTO : sysUserDTOList) {
            String organId = sysUserDTO.getOrganId();//获取用户所属机构
            OrganRelationDTO organ = sysOrganDao.findById(organId);
            if (organ != null) {
                //获取公司名称
                List<OrganRelationDTO> allOrgan = sysOrganDao.findAllParentOrgan(organId);
                String organGrade = organ.getOrganGrade();
                for (OrganRelationDTO organRelationDTO : allOrgan) {
                    if (organRelationDTO.getType().equals("公司") && organRelationDTO.getOrganGrade().equals(organGrade)) {
                        String company = organRelationDTO.getName();
                        sysUserDTO.setCompany(company);
                        break;
                    }

                }
            }
            SysArea sysArea = sysAreaDao.findByOrgan(organId);
            if (sysArea != null) {
                sysUserDTO.setCityName(sysArea.getFullName());
            }
        }
        return sysUserDTOList;
    }

    /**
     * 用户上传头像
     *
     * @param file
     * @param request
     * @return
     */
    @Override
    public JSONObject uploadImg(String uploadPath, MultipartFile file, HttpServletRequest request) {
        long size = file.getSize();
        logger.info("文件大小 size -->" + size);
        if (size > 10240000) {//1M
            throw new SsoException("请上传10M以内的文件");
        }
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        logger.info("fileName -->" + fileName);
        if (fileName.indexOf(".") <= 0) {
            throw new SsoException("未知文件, 请重新上传");
        }
        int lastNum = fileName.lastIndexOf(".");
        String suffix = fileName.substring(lastNum + 1, fileName.length());
        logger.info("suffix -->" + suffix);
        //重命名上传后的文件名
        String fileNewName = "img" + System.nanoTime() + "." + suffix;
        String fileUpdateName = fileName.replace(fileName, fileNewName);

        if (!FileUtils.IMG_FILTER.contains(suffix.toLowerCase())) {
            throw new SsoException("请上传后缀为(jpg,gif,png)类型的图片文件");
        }
        //定义上传路径
        //String path = request.getSession().getServletContext().getRealPath("upload/");
        String imgurl = uploadPath + fileUpdateName;
        //logger.info("path -->"+path);
        imgurl = imgurl.substring(1);
        logger.info("imgurl -->" + imgurl);
        try {
            FileUtils.uploadFile(file.getBytes(), uploadPath, fileNewName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SsoException("上传失败," + e.getMessage());
        }
        JSONObject json = new JSONObject();
        //返回json
        //json.put("fileName", fileUpdateName);
        json.put("pic", fileUpdateName);//图片路径
        return json;
    }

    @Override
    @CacheEvict(value = SysUserCache.CACHE_FIND_PAGE, allEntries = true)
    public boolean insertUser(SysUser user) {
        //先在uaa里创建用户，返回id
        UserDTO2 userDTO2 = new UserDTO2(user.getLoginName(), user.getPasswd());
        ResponseEntity<User> dto = feignClient.createUser(userDTO2);
        //System.out.println(dto.getStatusCodeValue());
        if (dto.getStatusCodeValue() != 201) {
            return false;
        }
        String uuid = dto.getBody().getId().toString();
        user.setId(uuid);
        user.setUpdateTime(new Date());
        user.setCreateUser("t");
        user.setPasswd("");
//      user.setChangePswdTime(new Date());
        user.setFreezeTime("1");//1为允许登录
        int i = userDao.insertSelective(user);
        List<String> roleids = user.getRoleids();
//        logger.info("userid-->" + user.getId()+", roleids -->"+ Arrays.toString(roleids));
        if (!StringUtils.isEmpty(roleids) && roleids.size() > 0) {
            List<String> list = CommonUtil.idLists(user.getId(), roleids);
            int x = sysRoleUserDao.insertBatch(list);
        }
        return i > 0;
    }

    /* (non-Javadoc)
     * @see 待修改
     */
    @Override
    @CacheEvict(value = SysUserCache.CACHE_FIND_PAGE, allEntries = true)
    public boolean updateUser(SysUser user) {
        UserDTO2 userDTO2 = new UserDTO2(Long.parseLong(user.getId()), user.getLoginName(), user.getPasswd());
        ResponseEntity<User> dto = feignClient.updateUserLogin(userDTO2);
        //System.out.println(dto.getStatusCodeValue());
        if (dto.getStatusCodeValue() != 200) {
            return false;
        }
        user.setLoginName(user.getLoginName());
        user.setCreateUser("t");
        user.setPasswd("");
        int i = userDao.updateUser(user);
        if (i > 0) {
            List<String> roleids = user.getRoleids();
            logger.info("roleids -->" + roleids);
            if (!StringUtils.isEmpty(roleids) && roleids.size() > 0) {
                sysRoleUserDao.deleteByUserid(user.getId());//先删除, 再添加
                List<String> list = CommonUtil.idLists(user.getId(), roleids);
                int x = sysRoleUserDao.insertBatch(list);
            }

            return true;
        }
        return false;
    }

}

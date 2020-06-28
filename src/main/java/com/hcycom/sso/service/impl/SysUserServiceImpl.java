package com.hcycom.sso.service.impl;

/*import com.hcycom.sso.access.AccessInterceptor;
import com.hcycom.sso.config.TokenConfig;
import com.hcycom.sso.controller.SsoAuthController;*/

import com.hcycom.sso.dao.SysUserDao;
import com.hcycom.sso.dao.permissions.*;
import com.hcycom.sso.domain.*;
import com.hcycom.sso.dto.OrganRelationDTO;
import com.hcycom.sso.dto.UserDTO;
import com.hcycom.sso.service.GatewayFeignClient;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.UaaFeignClient;
import com.hcycom.sso.utils.HttpRequestUtil;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.TokenVO;
import com.hcycom.sso.vo.UserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*import com.hcycom.sso.redis.CheckUserKey;
import com.hcycom.sso.redis.RedisService;
import com.hcycom.sso.redis.UserKey;*/

@Service
public class SysUserServiceImpl implements SysUserService, Runnable {

    final static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private UaaFeignClient uaaFeignClient;

    @Autowired
    private GatewayFeignClient gatewayFeignClient;


    @Autowired
    private SysAreaDao sysAreaDao;
    /*  @Autowired
    private RedisService redisService;*/
    @Autowired
    private SysFreezeIpDao sysFreezeIpDao;
    @Autowired
    private SysOrganDao sysOrganDao;
    @Autowired
    private SysSpecialtyOrganDao sysSpecialtyOrganDao;

    /*  @Value("${timer_mill}")
    private long timer_mill;*/
    static final Map<String, Long> TIMES = new ConcurrentHashMap<>();

    @Override
    public UserInfoVO getLoginUser(HttpServletRequest request, HttpServletResponse response) {

        String token = getToken(request);
        UserInfoVO user = this.getByToken(request, response, token);
        if (user != null) {
            //************
			/*redisService.expire(UserKey.token, token, TokenConfig.COOKIE_AGE);
           CookieUtils.writeCookie(response, TokenConfig.COOKIE_NAME, token, TokenConfig.COOKIE_AGE);

			 */

        }
        return user;
    }

    private TokenVO randomToken(HttpServletResponse response, String token, UserInfoVO user) {
        TokenVO t = new TokenVO();
        t.setToken(token);
        //********************
        /* t.setExpire(TokenConfig.COOKIE_AGE);//存活时间
         */
        addRedis(response, token, user);
        return t;
    }

    @Override
    public SysUser findUser(String telephone, String passwd) {
        SysUser user = new SysUser();
        user.setTelephone(telephone);
        user.setPasswd(passwd);

        SysUser sysUser = this.sysUserDao.findUser(user);
        if (sysUser != null) {
            String uid = sysUser.getId();     //获取id
            List<SysRole> roleList = sysRoleDao.findByUid(uid);

            if (roleList != null && roleList.size() > 0) {
                List<String> roleIds = new ArrayList<>();
                for (SysRole role : roleList) {
                    roleIds.add(role.getId());
                }
                sysUser.setRoleids(roleIds);
            } else {
                sysUser.setRoleids(null);
            }
            //获取地市
            String organ = sysUser.getOrganId();//获取用户所属机构
            SysArea sysArea = sysAreaDao.findByOrgan(organ);
            if (sysArea != null) {
                sysUser.setCityId(sysArea.getId());
            } else {
                sysUser.setCityId("");
            }

            return sysUser;
        }
        return null;
    }

    public SysUser findUser(String uid) {

        SysUser sysUser = userDao.getById(uid);
        if (sysUser != null) {
            List<SysRole> roleList = sysRoleDao.findByUid(uid);

            if (roleList != null && roleList.size() > 0) {
                List<String> roleIds = new ArrayList<>();
                for (SysRole role : roleList) {
                    roleIds.add(role.getId());
                }
                sysUser.setRoleids(roleIds);
            } else {
                sysUser.setRoleids(null);
            }
            //获取地市
            String organ = sysUser.getOrganId();//获取用户所属机构
            SysArea sysArea = sysAreaDao.findByOrgan(organ);
            if (sysArea != null) {
                sysUser.setCityId(sysArea.getId());
            } else {
                sysUser.setCityId("");
            }

            return sysUser;
        }
        return null;
    }

    //轮训，作用是检测重复登录用户和清除无用登录用户
    @Override
    public void run() {
        while (true) {
            try {
                //*******************
				/*  Thread.sleep(timer_mill);//毫秒
                //System.out.println("-------进入了定时器--------" + System.currentTimeMillis());
                Set<String> keys = redisService.getKeys(UserKey.token.getPrefix());
                if(keys != null && keys.size() > 0) {
                    Set<String> redisUserName = new HashSet<>();
                    Iterator<String> it = keys.iterator();
                    while(it.hasNext()) {
                        String s = it.next();
                        SysUser redisUser = redisService.get(s, SysUser.class);
                        if(redisUser != null) {
                            redisUserName.add(redisUser.getLoginName());
                        }
                    }
                    Map<String, String> maps = SsoAuthController.USER_LOGIN_NAME;
                    if(maps.size() > 0) {
                        for (Map.Entry<String, String> m : maps.entrySet()) {
                            String value = m.getValue();
                            //当缓存用户与集合中不一致时，清除集合中的不一致用户
                            if(!redisUserName.contains(value)) {
                                logger.info("USER_LOGIN_NAME中的用户有--"+maps);
                                logger.info("清除了集合中不一致的用户"+value);
                                SsoAuthController.USER_LOGIN_NAME.remove(m.getKey());
                            }
                        }
                    }

                } else {
                    SsoAuthController.USER_LOGIN_NAME.clear();
                }*/
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public UserInfoVO getByToken(HttpServletRequest request, HttpServletResponse response, String token) {
        UserInfoVO user = null;
		/*//*********************************
		  UserInfoVO user = redisService.get(UserKey.token, token, UserInfoVO.class);
            if(user != null) { String newToken = token;
               addRedis(response, newToken, user);
            }*/
        return user;
    }

    //获取cookie下的token
    @Override
    public String getToken(HttpServletRequest request) {
        String token = "";
        //**************
		/*String token = request.getParameter(TokenConfig.COOKIE_NAME);
        if(StringUtils.isEmpty(token)) {
            Cookie[] c = request.getCookies();
            if(c != null) {
                for (Cookie co: c) {
                    if (TokenConfig.COOKIE_NAME.equals(co.getName())) {
                        token = co.getValue();
                        break;
                    }
                }
            }
        }*/
        return token;
    }


    @Override
    public UserInfoVO getRedisUser() {
        UserDTO userDTO = uaaFeignClient.account();
        if (userDTO != null && userDTO.getId() != null) {
            SysUser sysUser = findUser(userDTO.getId().toString());
            UserInfoVO userInfoVO = new UserInfoVO(sysUser);
            SysArea area = sysAreaDao.findByOrgan(sysUser.getOrganId());
            if (area != null) {
                userInfoVO.setCityName(area.getName());
            } else {
                userInfoVO.setCityName("");
            }
            String organId = sysUser.getOrganId();//获取用户所属机构
            OrganRelationDTO organ = sysOrganDao.findById(organId);
            if (organ != null) {
                //获取公司名称
                List<OrganRelationDTO> allOrgan = sysOrganDao.findAllParentOrgan(organId);
                String organGrade = organ.getOrganGrade();
                for (OrganRelationDTO organRelationDTO : allOrgan) {
                    if (organRelationDTO.getType().equals("公司") && organRelationDTO.getOrganGrade().equals(organGrade)) {
                        String company = organRelationDTO.getName();
                        userInfoVO.setCompany(company);
                        break;
                    }

                }
            }
            //获取专业
            List<SysSpecialtyOrgan> list = sysSpecialtyOrganDao.select();
            if (list != null) {
                for (SysSpecialtyOrgan sysSpecialtyOrgan : list) {
                    if (sysSpecialtyOrgan.getOrganId().equals(sysUser.getOrganId())) {
                        userInfoVO.setSpecialty(sysSpecialtyOrgan.getSpecialtyId());
                        break;
                    }
                }
            } else {
                userInfoVO.setSpecialty("");
            }
            return userInfoVO;
        }
        return null;
    }

    @Override
    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        String ip = HttpRequestUtil.getIpAddr(request);
        String token = getToken(request);
        logger.info("ip为:" + ip + ", 进入了clearCookies  token -->" + token);
        //**************************************
		/*if(!StringUtils.isEmpty(token)) {
            redisService.delete(UserKey.token, token);
            CookieUtils.writeCookie(response, TokenConfig.COOKIE_NAME, null, 0);
        }*/
    }

    private void addRedis(HttpServletResponse response, String token, UserInfoVO user) {
        ////////************************
        //logger.info("进入了addRedis方法!!");
        //redisService.set(UserKey.token, token, user);
		/*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = HttpRequestUtil.getIpAddr(request);
        logger.info("ip为"+ip+" 当前的token为 ： "+token);*/

        //redisService.expire(UserKey.token, token, TokenConfig.COOKIE_AGE);
        //CookieUtils.writeCookie(response, TokenConfig.COOKIE_NAME, token, TokenConfig.COOKIE_AGE);
    }

    @Override
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        if (cookieName == null) {
            return null;
        }
        Cookie[] c = request.getCookies();
        if (c != null) {
            for (Cookie co : c) {
                if (cookieName.equals(co.getName())) {
                    return co.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public ResultVO<SysUser> checkUser(HttpServletRequest request, HttpServletResponse response) {
        String ip = HttpRequestUtil.getIpAddr(request);
        SysFreezeIp sysFreezeIp = sysFreezeIpDao.findByIp(ip);  //获取冻结的Ip
        if (sysFreezeIp != null) {
            String freezeIp = sysFreezeIp.getIp();
            if (ip.equals(freezeIp)) {
                return ResultVOUtil.error(1, "该IP禁止登陆");
            }
        }
	/*	System.out.println("---当前用户信息--"+r);
		gatewayFeignClient.authenticate(request, response, params);
	        //存储登录信息
	        if(r.getData()!=null) {
	            SysLoginLog sysLoginLog = new SysLoginLog();
	            String id = UUIDTool.getUUID();
	            String uid = r.getData().getId();
	            String fullName = r.getData().getFullName();
	            Date date = new Date();
	            sysLoginLog.setId(id);
	            sysLoginLog.setSysUserId(uid);
	            sysLoginLog.setIpAddr(ip);
	            sysLoginLog.setCreateUser(fullName);
	            sysLoginLog.setStartTime(date);
	            String remoteAddr = HttpRequestUtil.getIpAddr(request);
	            //获取ip归属
	            IPLocationUtil ipLocationUtil = new IPLocationUtil();
	            String addr = ipLocationUtil.getAddresses(remoteAddr);
	            String ipLocation = "";
	            String isp = "";
	            String[] arr = addr.split("\\s+");
	            ipLocation = arr[0];
	            if (arr.length > 1) {
	                isp = arr[1];
	            }
	            sysLoginLog.setIpLocation(ipLocation);
	            sysLoginLog.setIsp(isp);
	            sysLoginlogService.saveLog(sysLoginLog);
	        }*/


        return null;
    }

    @Override
    public SysUser getUser(String telephone) {
        SysUser user = new SysUser();
        user.setTelephone(telephone);
        SysUser sysUser = this.sysUserDao.getUser(user);
        if (sysUser != null) {
            return sysUser;
        } else {
            return null;
        }
    }

    @Override
    public List<OrganRelationDTO> getUserOrganInfo() {
        // TODO Auto-generated method stub
        List<OrganRelationDTO> count = new ArrayList<>();
        count = sysUserDao.getUserOrganInfo();
        return count;
    }

    /**
     * 这是一个测试
     */
    public void test() {

        System.out.println("这也是一个测试");

    }


    /**
     * 这是第二个测试
     */
    public void test02() {
        System.out.println("这是第二个");
        System.out.println("这也是个测试");
    }


}

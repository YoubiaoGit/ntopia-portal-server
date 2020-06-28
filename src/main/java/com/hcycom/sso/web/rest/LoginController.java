package com.hcycom.sso.web.rest;

import com.hcycom.sso.dao.permissions.SysFreezeIpDao;
import com.hcycom.sso.domain.SysFreezeIp;
import com.hcycom.sso.service.GatewayFeignClient;
import com.hcycom.sso.utils.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author LK
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private GatewayFeignClient gatewayFeignClient;

    @Autowired
    private SysFreezeIpDao sysFreezeIpDao;

    //过滤拉黑的ip
    //@CrossOrigin(origins = "*")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType
        .APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OAuth2AccessToken> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody
        Map<String, String> params) {
        //获取服务的ip地址
        String ip = HttpRequestUtil.getIpAddr(request);

        logger.info("ip 为" + ip + " 进入了login方法....");
        SysFreezeIp sysFreezeIp = sysFreezeIpDao.findByIp(ip);  //获取冻结的Ip
        if (sysFreezeIp != null) {
            String freezeIp = sysFreezeIp.getIp();
            if (ip.equals(freezeIp)) {
                logger.info("该IP禁止登陆:" + ip);
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        try {
            return gatewayFeignClient.authenticate(params);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

}

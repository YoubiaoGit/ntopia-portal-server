package com.hcycom.sso.web.rest;

import com.hcycom.sso.domain.Authority;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.domain.User;
import com.hcycom.sso.dto.UserDTO;
import com.hcycom.sso.dto.UserDTO2;
import com.hcycom.sso.service.GatewayFeignClient;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.UaaFeignClient;
import com.hcycom.sso.service.permissions.UserService;
import com.hcycom.sso.utils.*;
import com.hcycom.sso.vo.ResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * sso认证
 * @param <UserService>
 *
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-18 14:31
 */
@Api(tags = { "sso认证feign" }, description = "FeignTestController")
@RestController
@RequestMapping("/feign")
public class FeignTestController {

	final static Logger logger = LoggerFactory.getLogger(FeignTestController.class);
	@Autowired
	private UaaFeignClient uaaFeignClient;
	@Autowired
	private GatewayFeignClient gatewayFeignClient;

    @ApiOperation(value = "获取账户信息", notes = "获取账户")
	@GetMapping(value = "/getaccount")
	public UserDTO getaccount(HttpServletRequest request)  {

		return uaaFeignClient.account();
	}

	@ApiOperation(value = "获取授权信息", notes = "获取授权")
	@GetMapping(value = "/getAuthorities")
	public String getAuthorities(HttpServletRequest request)  {
		System.out.println(uaaFeignClient.authorities());
		System.out.println("11111111111111");
		return "";
	}

	@ApiOperation(value = "创建用户", notes = "创建用户")
    @ApiImplicitParam(name = "userDTO", value = "UserDTO2类", dataType = "UserDTO2")
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO2 userDTO) throws URISyntaxException {


		return uaaFeignClient.createUser(userDTO);
	}

    @ApiOperation(value = "获取授权", notes = "获取授权")
    @ApiImplicitParam(name = "params", value = "参数", dataType = "Map")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType
		        .APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OAuth2AccessToken> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody
        Map<String, String> params) {
            return gatewayFeignClient.authenticate( params);
    }

}

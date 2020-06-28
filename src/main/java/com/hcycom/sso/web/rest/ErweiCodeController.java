/*package com.hcycom.sso.web.rest;

import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.UserService;
import com.hcycom.sso.utils.*;
import com.hcycom.sso.vo.ResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

*//**
 *
 * sso认证
 * @param <UserService>
 * 
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-18 14:31
 *//*
@Api(tags= {"二维码登录接口"})
@RestController
@RequestMapping("/api/erweicode")
public class ErweiCodeController {
	final static Logger logger = LoggerFactory.getLogger(ErweiCodeController.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private SysUserService sysUserService;

	@Value("${web.upload-path}")
    private String path;//上传路径
	
	private static HashMap<String, SysUser> loginUserMap = new HashMap<String, SysUser>();

	*//**
	 * 生成二维码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	@ApiOperation(value="生成二维码")
	@GetMapping(value = "/getTwoDimensionCode")
	public String getTwoDimensionCode(HttpServletRequest request, @ApiParam(value="传入文件")MultipartFile file,
			@ApiParam(value="传入二维码")String erWeiCode) throws Exception {
		String uuid = UUID.randomUUID().toString();
		String content = erWeiCode+"erweicode/phoneLogin?uuid=" + uuid + "&xx=" + new Date().getTime();
		logger.info("**********************"+content);
		String fileName = uuid + ".png";
		File imageFile = new File(path,fileName);
		//String file1 = path+fileName;
		//logger.info(file1);
		logger.info("*************"+path+"***********"+fileName);
		TwoDimensionCodeUtil twoDimensionCode = new TwoDimensionCodeUtil();
		twoDimensionCode.createQRCode(content, 300, 300, 1, path,imageFile);
		return uuid;
	}

	*//**
	 * 检测二维码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*

	@ApiOperation(value="检测二维码")
	@GetMapping(value = "/longConnectionCheck")
	public ResultVO<SysUser> longConnectionCheck(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String uuid = request.getParameter("uuid");
		// 检测登录
		System.out.println("**********************"+uuid);
		SysUser sysUser = loginUserMap.get(uuid);
		System.out.println(sysUser);
		if (sysUser != null) {
			return ResultVOUtil.success(sysUser);
		}else{
			ResultVO resultVO = new ResultVO();
			resultVO.setCode(1);
			resultVO.setMessage("登录失败");
			return resultVO;
		}
	}

	*//**
	 * 验证二维码登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	@ApiOperation(value="验证二维码登录")
	@GetMapping(value = "/phoneLogin")
	public ResultVO<SysUser> phoneLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uuid = request.getParameter("uuid");
		String telephone = request.getParameter("telephone");
		String passwd = request.getParameter("passwd");
		SysUser sysUser = new SysUser();
		sysUser=sysUserService.getUser(telephone);
		System.out.println("*********************"+sysUser);
		if(sysUser!=null){
			loginUserMap.put(uuid, sysUser);
			System.out.println("*********************"+sysUser);
			return ResultVOUtil.success(sysUser);
		}else{
			ResultVO resultVO = new ResultVO();
			resultVO.setCode(1);
			resultVO.setMessage("登录失败");
			return resultVO;
		}
	}
}
*/
package com.hcycom.sso.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.dao.SysUserDao;
import com.hcycom.sso.dao.permissions.UserDao;
import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.CompanyDTO;
import com.hcycom.sso.dto.DepartmentDTO;
import com.hcycom.sso.dto.SysUserDTO;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.UaaFeignClient;
import com.hcycom.sso.service.permissions.*;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 * 用户管理
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-25 18:24
 */
@Api(tags = { "系统用户管理接口" }, description = "SysUserController")
@RestController
@RequestMapping("/api/permissions/user")
public class SysUserController {
    final static Logger logger = LoggerFactory.getLogger(SysUserController.class);
    static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\\\d{8}$";
    //11
    @Autowired
    private UserService userService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysOrganService sysOrganService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
	private UaaFeignClient uaaFeignClient;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private UserDao userDao;
    @Value("${web.upload-path}")
    private String path;//上传路径
    @Value("${web.province}")
    private String province;

    @ApiOperation(value = "查询系统用户信息", notes = "实现分页和模糊查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyWord", value = "关键字", required = false, dataType = "String"),
        @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", defaultValue = "1"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "int", defaultValue = "8")
    })
    @GetMapping(value = "find")
    public ResultVO<PageInfo<SysUserDTO>> getList(String keyWord, @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "8") int pageSize) {
        PageInfo<SysUserDTO> info = this.userService.findByPage(keyWord, pageNum,pageSize);
        return ResultVOUtil.success(info);
    }

    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户")
    @GetMapping(value = "getCurrentUser")
    public ResultVO getCurrentUser() {
        UserInfoVO user = this.sysUserService.getRedisUser();

        user.setProvince(province);

        return ResultVOUtil.success(user);
    }

    @ApiOperation(value = "查询部门和公司信息", notes = "根据parentId查询")
    @ApiImplicitParam(name = "parentId", value = "parentId", dataType = "String")
    @GetMapping("findDepartAndCompany")
    public ResultVO<JSONObject> findDepartAndCompany(String parentId) {
        //公司
        List<CompanyDTO> cpList = sysOrganService.findAllCompany();
        //部门
        List<DepartmentDTO> dpList = sysOrganService.findAllDepartment(parentId);
        JSONObject json = new JSONObject();
        json.put("cpList", cpList);
        json.put("dpList", dpList);
        return ResultVOUtil.success(json);
    }

    @ApiOperation(value = "删除用户", notes = "删除指定用户")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @Transactional
    @GetMapping(value = "delete")
    public ResultVO<String> deleteUserById(@RequestParam String id) {
        userService.deleteById(id);
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "查询用户详情", notes = "查询用户详情")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findById")
    public ResultVO<JSONObject> getById(@RequestParam String id) {
        JSONObject obj = findCondition(id);
        SysUser user = userService.getById(id);
//        if(!StringUtils.isEmpty(user.getDepartment())){
//            String[] departArry = user.getDepartment().split(",");
//            obj.put("departArry",departArry);
//        }else{
//            obj.put("departArry",null);
//        }
//        String specialIds = user.getSpecialId();
//        String[] specialId = specialIds.split(",");
//        obj.put("checkedSpecial",specialId);
//        if(specialId !=null ||"".equals(specialId) ){
//            SysSpecialty sysSpecialty =  sysSpecialtyService.findOne(specialId);
//            String specialName = sysSpecialty.getName();
//            obj.put("specialName",specialName);
//        }
        obj.put("user", user);
        return ResultVOUtil.success(obj);
    }
//    @GetMapping(value = "findUserByArea")
//    public ResultVO<List<SysUser>> getUserByArea(@RequestParam String cityCode) {
//        List<SysUser> listUser = userService.getUserByArea(cityCode);
//        return ResultVOUtil.success(listUser);
//    }

    @ApiOperation(value = "查询角色名称", notes = "查询角色名称")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findRoleName")
    public ResultVO<JSONObject> findRoleName(String id) {
        return ResultVOUtil.success(findCondition(id));
    }

    private JSONObject findCondition(String id) {
        List<SysRole> list = sysRoleService.findRoleName(id);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SysRole.class, "id","name");
        String roleStr = JSONObject.toJSONString(list, filter);
        JSONObject json = new JSONObject();
        if(id == null) {
            json.put("roleName", roleStr);
            return json;
        }
        List<String> roleids = sysRoleService.findRoleids(id);
        List<SysRole> checkRoleList = new ArrayList<>();
        if(roleids.size()>0){
            SysRole role = new SysRole();
                for (String roleId:roleids) {
                    SysRole sysRole = sysRoleService.findById(roleId);
                    if(sysRole != null) {
                    	String name = sysRole.getName();
                        role.setId(roleId);
                        role.setName(name);
                        checkRoleList.add(sysRole);
                    }
                }
            json.put("checkRoleList",checkRoleList);
        }
        json.put("checkedId", roleids);
        return json;
    }

    //处理文件上传
    @ApiOperation(value = "处理文件上传", notes = "上传文件")
    @ApiImplicitParam(name = "file", value = "上传文件",  dataType = "MultipartFile")
    @PostMapping(value="uploadImg")
    public ResultVO<JSONObject> uploadImg(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) {
        JSONObject json = userService.uploadImg(path, file, request);

        return ResultVOUtil.success(json);
    }

    @ApiOperation(value = "添加/更新用户信息", notes = "添加/更新用户操作")
    @ApiImplicitParam(name = "user", value = "SysUser类", dataType = "SysUser")
    @Transactional
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody @Valid  SysUser user) {
		/*if(StringUtils.isEmpty(user.getPasswd())) {
			return ResultVOUtil.error(1,"请输入密码");

		}*/
		/*if(Pattern.matches(REGEX_MOBILE, user.getTelephone())){
            return ResultVOUtil.error(1,"请输入正确的手机格式");
        };
		 */
		if(org.apache.commons.lang3.StringUtils.isBlank(user.getLoginName() )){
			return ResultVOUtil.error(1,"请输入正确的用户名");
		}
		SysArea sysArea = sysAreaService.findByOrgan(user.getOrganId());
		if (sysArea != null && sysArea.getFullName() != null){
			user.setCityName(sysArea.getFullName());
		}


		user.setFreezeTime("1");
		//user.setLoginName(user.getLoginName());

		if(StringUtils.isEmpty(user.getId())) {
			String telephone = user.getTelephone();
			List<SysUser>  list= sysUserDao.findUserbyloginName(user.getLoginName());
			System.out.println(telephone+"    "+list.toString());
			if (list != null && list.size() >0) {
				return ResultVOUtil.error(1, "用户名已被添加");
			}

			if (userService.insertUser(user)){
				return  ResultVOUtil.success();
			}else{
				return  ResultVOUtil.error(1, "添加失败");
			}
		}
		SysUser sysUser= userDao.getById(user.getId());
		if (!user.getLoginName().equals(sysUser.getLoginName())){
			List<SysUser>  list= sysUserDao.findUserbyloginName(user.getLoginName());
			System.out.println(user.getTelephone()+"    "+list.toString());
			if (list != null && list.size() >0){
				return ResultVOUtil.error(1,"用户名已被添加");
			}
		}

		boolean isUpdateMySelf = userService.updateUser(user);
		JSONObject json = new JSONObject();
		if(isUpdateMySelf) {
			json.put("isUpdateMySelf", true);
		} else {
			json.put("isUpdateMySelf", false);
		}
		return  ResultVOUtil.success(json.toString());

	}

    //修改用户密码
   /* @ApiOperation(value = "修改用户密码", notes = "修改指定用户密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String"),
        @ApiImplicitParam(name = "oldCode", value = "旧密码", required = true, dataType = "String"),
        @ApiImplicitParam(name = "newCode", value = "新密码", required = true, dataType = "String")
    })
    @GetMapping(value = "changePassword")
    public ResultVO<JSONObject> changePassword(String id,String oldCode,String newCode) {
        boolean isUpdateMySelf=  userService.updatePassword(id,oldCode,newCode);
        JSONObject json = new JSONObject();
        if(isUpdateMySelf) {
            json.put("isUpdateMySelf", true);
        } else {
            json.put("isUpdateMySelf", false);
        }
        return  ResultVOUtil.success(json.toString());
    }*/
}

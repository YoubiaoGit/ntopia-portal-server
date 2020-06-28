package com.hcycom.sso.web.rest;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.dao.SysUserDao;
import com.hcycom.sso.dao.permissions.UserDao;
import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.CompanyDTO;
import com.hcycom.sso.dto.DepartmentDTO;
import com.hcycom.sso.dto.SysUserDTO;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.SysAreaService;
import com.hcycom.sso.service.permissions.SysOrganService;
import com.hcycom.sso.service.permissions.SysRoleService;
import com.hcycom.sso.service.permissions.SysSpecialtyService;
import com.hcycom.sso.service.permissions.UserService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.UserInfoVO;

/**
 * 用户管理
 *
 * @Author: lirong
 * @Date:Created in 2019-01-03
 */
@Api(tags = {"用户管理接口"}, description = "UserController")
@RestController
@RequestMapping("/api/user")
public class UserController {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);
    static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\\\d{8}$";
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
    private SysUserDao sysUserDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SysSpecialtyService sysSpecialtyService;

    @Value("${web.upload-path}")
    private String path;//上传路径

    @ApiOperation(value = "查询用户信息", notes = "实现分页和模糊查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyWord", value = "关键字", required = false, dataType = "String"),
        @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", defaultValue = "1"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "int", defaultValue = "8")
    })
    @OperationLog(value = "菜单管理-列表")
    @GetMapping(value = "find")
    public ResultVO<PageInfo<SysUserDTO>> getList(String keyWord, @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "8") int pageSize) {
        PageInfo<SysUserDTO> info = this.userService.findByPage(keyWord, pageNum, pageSize);
        return ResultVOUtil.success(info);
    }

    @ApiOperation(value = "根据关键字查询用户", notes = "实现模糊查询")
    @ApiImplicitParam(name = "keyWord", value = "关键字", required = false, dataType = "String")
    @GetMapping(value = "findAll")
    public ResultVO<List<SysUserDTO>> findAll(String keyWord) {
        List<SysUserDTO> info = this.userService.findAll(keyWord);
        return ResultVOUtil.success(info);
    }

    //获取当前用户信息
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息")
    @GetMapping(value = "userinfo")
    public ResultVO userinfo() {
        UserInfoVO user = this.sysUserService.getRedisUser();
        return ResultVOUtil.success(user);
    }

    //获取当前组织机构的用户
    @ApiOperation(value = "获取当前组织机构的用户", notes = "根据机构id查询机构的用户")
    @ApiImplicitParam(name = "organId", value = "机构id", dataType = "String")
    @GetMapping(value = "organUsers")
    public ResultVO organUsers(String organId) {
        List<String> userList = this.userService.findUserByOrgan(organId);
        return ResultVOUtil.success(userList);
    }

    @ApiOperation(value = "查询部门和公司", notes = "根据parentId查询")
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

    @ApiOperation(value = "删除指定用户", notes = "删除指定用户")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @Transactional
    @GetMapping(value = "delete")
    @OperationLog(value = "")
    public ResultVO<String> deleteUserById(@RequestParam String id) {
        userService.deleteById(id);
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "查询用户详情", notes = "查询用户详情")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findById")
    @OperationLog(value = "用户-详情")
    public ResultVO<JSONObject> getById(@RequestParam String id) {
        JSONObject obj = findCondition(id);
        SysUser user = userService.getById(id);
        obj.put("user", user);
        return ResultVOUtil.success(obj);
    }

    @ApiOperation(value = "查询角色名称", notes = "查询角色名称")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findRoleName")
    public ResultVO<JSONObject> findRoleName(String id) {
        return ResultVOUtil.success(findCondition(id));
    }

    private JSONObject findCondition(String id) {
        List<SysRole> list = sysRoleService.findRoleName(id);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SysRole.class, "id", "name");
        String roleStr = JSONObject.toJSONString(list, filter);
        JSONObject json = new JSONObject();
        if (id == null) {
            json.put("roleName", roleStr);
            return json;
        }
        List<String> roleids = sysRoleService.findRoleids(id);
        json.put("checkedId", roleids);
        return json;
    }

    //处理文件上传
    @ApiOperation(value = "处理文件上传", notes = "上传文件")
    @ApiImplicitParam(name = "file", value = "上传文件", paramType = "form", dataType = "File")
    @PostMapping(value = "uploadImg")
    public ResultVO<JSONObject> uploadImg(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) {
        JSONObject json = userService.uploadImg(path, file, request);

        return ResultVOUtil.success(json);
    }

    @ApiOperation(value = "根据用户名查用户", notes = "根据用户名查用户")
    @ApiImplicitParam(name = "loginname", value = "loginname", dataType = "String")
    @GetMapping(value = "findLoginame")
    public ResultVO<List<SysUser>> findLoginame(String loginname) {
        List<SysUser> list = sysUserDao.findUserbyloginName(loginname);
		/*if (list != null && list.size() >0) {
			return ;
		}*/
        return ResultVOUtil.success(list);
    }

    @ApiOperation(value = "添加/更新用户信息", notes = "添加/更新用户操作")
    @ApiImplicitParam(name = "user", value = "SysUser类", dataType = "SysUser")
    @Transactional
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody @Valid SysUser user) {
		/*if(StringUtils.isEmpty(user.getPasswd())) {
			return ResultVOUtil.error(1,"请输入密码");

		}*/
		/*if(Pattern.matches(REGEX_MOBILE, user.getTelephone())){
            return ResultVOUtil.error(1,"请输入正确的手机格式");
        };
		 */
        if (org.apache.commons.lang3.StringUtils.isBlank(user.getLoginName())) {
            return ResultVOUtil.error(1, "请输入正确的用户名");
        }
        SysArea sysArea = sysAreaService.findByOrgan(user.getOrganId());
        if (sysArea != null && sysArea.getFullName() != null) {
            user.setCityName(sysArea.getFullName());
        }
        user.setFreezeTime("1");
        //user.setLoginName(user.getLoginName());

        if (StringUtils.isEmpty(user.getId())) {
            String telephone = user.getTelephone();
            List<SysUser> list = sysUserDao.findUserbyloginName(user.getLoginName());
            System.out.println(telephone + "    " + list.toString());
            if (list != null && list.size() > 0) {
                return ResultVOUtil.error(1, "用户名已被添加");
            }

            if (userService.insertUser(user)) {
                return ResultVOUtil.success();
            } else {
                return ResultVOUtil.error(1, "添加失败");
            }
        }
        SysUser sysUser = userDao.getById(user.getId());
        if (!user.getLoginName().equals(sysUser.getLoginName())) {
            List<SysUser> list = sysUserDao.findUserbyloginName(user.getLoginName());
            System.out.println(user.getTelephone() + "    " + list.toString());
            if (list != null && list.size() > 0) {
                return ResultVOUtil.error(1, "用户名已被添加");
            }
        }

        boolean isUpdateMySelf = userService.updateUser(user);
        JSONObject json = new JSONObject();
        if (isUpdateMySelf) {
            json.put("isUpdateMySelf", true);
        } else {
            json.put("isUpdateMySelf", false);
        }
        return ResultVOUtil.success(json.toString());

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

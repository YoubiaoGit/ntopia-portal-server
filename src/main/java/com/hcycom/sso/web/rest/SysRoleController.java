package com.hcycom.sso.web.rest;


import com.github.pagehelper.PageInfo;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.SysMenu;
import com.hcycom.sso.domain.SysRole;
import com.hcycom.sso.dto.OrganRelationDTO;
import com.hcycom.sso.dto.RoleDTO;
import com.hcycom.sso.service.permissions.SysMenuService;
import com.hcycom.sso.service.permissions.SysOrganService;
import com.hcycom.sso.service.permissions.SysRoleService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
/**
 *
 * 角色
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-25 18:24
 */
@Api(tags = { "角色管理接口" }, description = "SysRoleController")
@RestController
@RequestMapping("/api/permissions/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysOrganService sysOrganService;
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation(value = "查询角色信息", notes = "实现分页和模糊查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyWords", value = "关键字", required = false, dataType = "String"),
        @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", defaultValue = "1"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "int", defaultValue = "8")
    })
    @OperationLog(value = "系统-角色管理列表")
    @GetMapping(value = "find")
    public ResultVO<PageInfo<RoleDTO>> findByPage(String keyWords, @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "8") int pageSize) {
        PageInfo<RoleDTO> page = sysRoleService.findByPage(keyWords,pageNum,pageSize);
        return ResultVOUtil.success(page);
    }

    @ApiOperation(value = "查询角色", notes = "根据id查询角色")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findById")
    public ResultVO<SysRole> findById(@RequestParam String id) {
        SysRole role = sysRoleService.findById(id);
        return ResultVOUtil.success(role);
    }

    @ApiOperation(value = "添加/修改角色信息", notes = "添加/修改角色")
    @ApiImplicitParam(name = "role", value = "SysRole类", dataType = "SysRole")
    @Transactional
    @OperationLog(value = "系统-保存角色数据")
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody @Valid SysRole role) {
        if(StringUtils.isEmpty(role.getId())) {
            sysRoleService.insertRole(role);
        } else {
            sysRoleService.updateRole(role);
        }

        return  ResultVOUtil.success();
    }

    @ApiOperation(value = "删除角色信息", notes = "删除角色")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @Transactional
    @GetMapping(value = "delete")
    public ResultVO<String> delete(@RequestParam String id) {
            sysRoleService.deleteRole(id);
            return ResultVOUtil.success();
    }

    @ApiOperation(value = "查询菜单信息", notes = "查询菜单信息")
    @ApiImplicitParam(name = "roleid", value = "角色id", dataType = "String")
    @GetMapping(value = "findMenuByRoleid")
    public ResultVO<List<SysMenu>> findMenuByRoleid(String roleid) {
        List<SysMenu> lists = sysMenuService.findByRoleId(roleid);
        return ResultVOUtil.success(lists);
    }

    //根据角色查用户
    @ApiOperation(value = "根据角色查询用户", notes = "根据角色id查询用户")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "userByRole")
    public  List<String> userByRole(String id){
        return sysRoleService.userByRole(id);
    }

    //获取其他依赖的数据
    @ApiOperation(value = "获取角色的依赖数据", notes = "获取其它的依赖数据")
    @ApiImplicitParam(name = "id", value = "id", dataType = "Integer")
    @GetMapping(value = "findCondition")
    public  ResultVO<List<OrganRelationDTO>> findCondition(Integer id) {
        List<OrganRelationDTO> orgList = sysOrganService.findAll(null);//机构
       // List<SysMenu> menuList = sysMenuService.findAll(null);
        //JSONObject obj = new JSONObject();
        //obj.put("orgList", orgList);
        //obj.put("menuList", menuList);
        /*if(id != null) {
            List<SysRoleMenu> roleMenuList = sysRoleMenuService.findMenuIdByRoleid(id);
            obj.put("roleMenuList", roleMenuList);
        }*/
        //obj = JSONObject.parseObject(obj.toString());
        return ResultVOUtil.success(orgList);
    }

    /**
     * 添加或修改角色时，查询角色名称是否已经存在
     * @param id
     * @param name
     * @return
     */
    @ApiOperation(value = "添加或修改角色时，查询角色名称是否已经存在")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色id"),
        @ApiImplicitParam(name = "name", value = "角色名称")
    })
    @RequestMapping(value = "isExistRoleName", method = RequestMethod.GET)
    public ResultVO<String> isExistRoleName(@RequestParam(required = false) String id, @RequestParam String name){
        sysRoleService.isExistRoleName(id, name);
        return ResultVOUtil.success();
    }

}

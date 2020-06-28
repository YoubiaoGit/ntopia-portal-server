package com.hcycom.sso.web.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.SysMenu;
import com.hcycom.sso.dto.MenuDTO;
import com.hcycom.sso.dto.SysUserDTO;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.SysMenuService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-25 18:24
 */
@Api(tags = { "菜单管理接口" }, description = "SysMenuController")
@RestController
@RequestMapping("/api/permissions/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "查询系统菜单", notes = "查询系统菜单")
    @OperationLog(value = "系统-菜单管理列表")
    @GetMapping(value = "findMainMenu")
    public ResultVO<List<SysMenu>> findParentMenu() {
        List<SysMenu> plist = new ArrayList<SysMenu>();
        UserInfoVO user = this.sysUserService.getRedisUser();
        List<String> roleids = user.getRoleids();
        if(roleids!=null&&roleids.size()>0){
            plist = sysMenuService.findParentMenu(roleids);
        }else{
            plist = sysMenuService.findParentMenu(null);
        }
        return ResultVOUtil.success(plist);
    }

    @ApiOperation(value = "查询菜单信息", notes = "根据id查询菜单")
    @ApiImplicitParam(name = "pid", value = "菜单id", dataType = "String")
    @GetMapping(value = "findMenuByPid")
    public ResultVO<List<SysMenu>> findMenuByPid(@RequestParam String pid) {
        UserInfoVO user = this.sysUserService.getRedisUser();
        List<String> roleids = user.getRoleids();
        Map<String,Object> clist = sysMenuService.findMenuByPid(pid,roleids);
        return ResultVOUtil.success(clist);
    }

    @ApiOperation(value = "菜单列表展示", notes = "菜单列表")
    @GetMapping(value = "menuList")
    public ResultVO<JSONObject> menuList() {
        UserInfoVO user = this.sysUserService.getRedisUser();
        List<String> roleids = user.getRoleids();
        List<SysMenu> plist = sysMenuService.findParentMenu(roleids);
        List<SysMenu> clist = sysMenuService.findChildMenu();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SysMenu.class, "id","name", "title", "path", "icon",
               "url",  "parentId", "supername");
        String jsonStr1 = JSONArray.toJSONString(plist, filter);
        String jsonStr2 = JSONArray.toJSONString(clist, filter);
        JSONObject json = new JSONObject();
        json.put("productDate", jsonStr1);
        json.put("viewsDate", jsonStr2);

        return ResultVOUtil.success(json);
    }

    @ApiOperation(value = "查询菜单信息", notes = "查询菜单")
    @GetMapping(value = "find")
    public ResultVO<List<SysMenu>> findAll() {
        List<SysMenu> list = sysMenuService.findAll();
        return ResultVOUtil.success(list);
    }

    @ApiOperation(value = "查询菜单", notes = "根据id查询菜单")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findById")
    public ResultVO<SysMenu> findById(@RequestParam String id) {
        SysMenu menu = sysMenuService.findById(id);
        return ResultVOUtil.success(menu);

    }

    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @Transactional
    @GetMapping(value = "delete")
    public ResultVO<String> delete(@RequestParam String id) {
        sysMenuService.deleteMenu(id);
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "保存菜单数据", notes = "添加/修改菜单数据")
    @ApiImplicitParam(name = "menu", value = "SysMenu类", dataType = "SysMenu")
    @OperationLog(value = "系统-保存菜单数据")
    @Transactional
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody @Valid SysMenu menu) {
        if(menu.getId() == null) {
            sysMenuService.insertMenu(menu);
        } else {
            sysMenuService.updateMenu(menu);
        }
        return ResultVOUtil.success();
    }

    //根据Id查询父元素
    @ApiOperation(value = "查询父元素", notes = "根据id查询父元素")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findByParent")
    public ResultVO<String> findByParent(@RequestParam String id){
        MenuDTO sysMenu = sysMenuService.findParent(id);
        return ResultVOUtil.success(sysMenu);
    }
    
    @ApiOperation(value = "添加/编辑菜单路由名称查重")
    @OperationLog(value = "系统-添加/编辑菜单路由名称查重")
    @Transactional
    @GetMapping(value = "checkRepeat")    
    public ResultVO<String> checkRepeat(@ApiParam(value="id")@RequestParam(required = false) String id , 
    		@ApiParam("父菜单id")@RequestParam String pid,
    		@ApiParam("路由名称")@RequestParam String name) {
    	
    	return sysMenuService.checkRepeat(id, pid, name);
    }
    
}

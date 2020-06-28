package com.hcycom.sso.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.domain.SysMenu;
import com.hcycom.sso.domain.SysRoleMenu;
import com.hcycom.sso.dto.OrganDTO;
import com.hcycom.sso.dto.SysUserDTO;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.SysAreaService;
import com.hcycom.sso.service.permissions.SysOrganService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import com.hcycom.sso.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 * 区域
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-25 18:23
 */
@Api(tags = { "区域管理接口" }, description = "SysAreaController")
@RestController
@RequestMapping("/api/permissions/area")
public class SysAreaController {
    @Autowired
    private SysAreaService sysAreaService;

    @ApiOperation(value = "查询区域信息", notes = "实现模糊查询")
    @ApiImplicitParam(name = "keywords", value = "关键字", required = false, dataType = "String")
    @OperationLog(value = "系统-区域管理列表")
    @GetMapping(value = "find")
    public ResultVO<List<SysArea>> findAll(String keywords) {
        //获取所有area列表
            List<SysArea> allList = sysAreaService.findAll(keywords);
//            List<SysArea> childArea = new ArrayList<>();
//            UserInfoVO user = this.sysUserService.getRedisUser();
//            String cityId = user.getCityId();
//            SysArea sysArea = sysAreaService.findById(cityId);
//            String pid = sysArea.getParentId();
//            if(!StringUtils.isEmpty(pid)){
//                for (SysArea sysArea1:allList){
//                    if(sysArea1.getId().equals(pid)){
//                        childArea.add(sysArea1);
//                        break;
//                    }
//                }
//            }
        return ResultVOUtil.success(allList);
    }

    @ApiOperation(value = "添加/修改区域信息", notes = "添加/修改区域信息")
    @ApiImplicitParam(name = "area", value = "SysArea类", dataType = "SysArea")
    @Transactional
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody @Valid @ApiParam("区域信息实体")SysArea area) {
        if(StringUtils.isEmpty(area.getId())) {
            sysAreaService.insertArea(area);
        } else {
            sysAreaService.updateArea(area);
        }
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "删除区域实现", notes = "删除区域")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @GetMapping(value = "delete")
    public  ResultVO<String> delete(@RequestParam String id) {
        sysAreaService.deleteArea(id);
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "查询区域信息", notes = "查询区域")
    @ApiImplicitParam(name = "code", value = "编码", required = false, dataType = "String")
    @GetMapping(value = "findArea")
    public ResultVO<List<SysArea>> findArea(String code) {
        if(StringUtils.isEmpty(code)) {
            return ResultVOUtil.success(sysAreaService.findAllTopArea());
        }
        return ResultVOUtil.success(sysAreaService.childArea(code));
    }

    @ApiOperation(value = "查询区域", notes = "根据id查询区域")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findById")
    public  ResultVO<SysArea> findById(@RequestParam String id) {
        SysArea area = sysAreaService.findById(id);
        return ResultVOUtil.success(area);
    }


    //获取其他依赖的数据
//    @GetMapping(value = "findCondition")
//    public  ResultVO<JSONObject> findCondition() {
//        List<SysArea> areaList = sysAreaService.findAll(null);
//        List<String> areaType = sysAreaService.findAreaType();
//        Supplier<JSONObject> s = JSONObject::new;
//        JSONObject obj =  s.get();
//        obj.put("areaList", areaList);
//        obj.put("areaType", areaType);
//        obj = JSONObject.parseObject(obj.toString());
//        return ResultVOUtil.success(obj);
//    }



}

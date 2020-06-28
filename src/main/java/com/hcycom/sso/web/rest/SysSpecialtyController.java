package com.hcycom.sso.web.rest;

import com.hcycom.sso.domain.SysSpecialty;
import com.hcycom.sso.service.permissions.SysSpecialtyService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:lirong
 * @Date:Created
 */
@Api(tags = { "专业接口" }, description = "SysSpecialtyController")
@RestController
@RequestMapping("/api/specialty")
public class SysSpecialtyController {

    @Autowired
    private SysSpecialtyService sysSpecialtyService;

    /**
     * 获取所属专业列表
     * @return
     */
    @ApiOperation(value = "获取所属专业列表", notes = "获取所属专业列表")
    @GetMapping("/list")
    public ResultVO<List<SysSpecialty>> findAll(){
        List<SysSpecialty> list = sysSpecialtyService.findAll();
        return ResultVOUtil.success(list);
    }

    /**
     * 根据Id获取网管
     * @param id
     * @return
     */
    @ApiOperation(value = "获取网管", notes = "根据id获取网管")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping("/findById")
    public ResultVO<SysSpecialty> findById(String id){
        SysSpecialty sysSpecialty = sysSpecialtyService.findOne(id);
        return  ResultVOUtil.success(sysSpecialty);
    }
}

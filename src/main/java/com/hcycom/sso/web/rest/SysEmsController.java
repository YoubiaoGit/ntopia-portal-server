package com.hcycom.sso.web.rest;

import com.hcycom.sso.domain.SysEms;
import com.hcycom.sso.service.permissions.SysEmsService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@Api(tags = { "网关管理接口" }, description = "SysEmsController")
@RestController
@RequestMapping("/api/ems")
public class SysEmsController {

    @Autowired
    private SysEmsService sysEmsService;

    /**
     * 获取网管列表
     * @return
     */
    @ApiOperation(value = "获取网管列表", notes = "获取网管列表")
    @GetMapping("/list")
    public ResultVO<List<SysEms>> findAll(){
        List<SysEms> list = sysEmsService.findAll();
        return ResultVOUtil.success(list);
    }

    /**
     * 根据id获取网管
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取网管", notes = "根据id获取网管")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping("/findById")
    public ResultVO<SysEms> findById(String id){
        SysEms sysEms = sysEmsService.findByOne(id);
        return ResultVOUtil.success(sysEms);
    }
}

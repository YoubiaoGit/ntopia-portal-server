package com.hcycom.sso.web.rest;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.SysFreezeIp;
import com.hcycom.sso.service.permissions.SysFreezeIpService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户登录ip记录
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 17:49
 */
@Api(tags = { "登录设置接口" }, description = "SysFreezeIpController")
@RestController
@RequestMapping("/api/permissions/loginip")
public class SysFreezeIpController {
    @Autowired
    private SysFreezeIpService sysFreezeIpService;

    @ApiOperation(value = "查询登录设置列表", notes = "查询登录设置列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keywords", value = "关键字", required = false, dataType = "String"),
        @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", defaultValue = "1"),
        @ApiImplicitParam(name = "pageSize", value = "页面显示条数", dataType = "int", defaultValue = "8")
    })
    @OperationLog(value = "系统-登录设置列表")
    @GetMapping(value = "find")
    public ResultVO<PageInfo<SysFreezeIp>> findByPage(String keywords, @RequestParam(required = false, defaultValue = "1") int pageNum, @RequestParam(required = false, defaultValue = "8") int pageSize) {
        PageInfo<SysFreezeIp> info = sysFreezeIpService.findByPage(keywords, pageNum, pageSize);
        return ResultVOUtil.success(info);

    }

    /**
     * 是否启用
     * @param id
     * @param using
     * @return
     */
    @ApiOperation(value = "是否启用操作", notes = "是否启用")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "id", dataType = "String"),
        @ApiImplicitParam(name = "using", value = "是否启用1/0", dataType = "String")
    })
    @PostMapping(value = "updateUsing")
    public ResultVO<Boolean> updateUsing(@RequestParam String id, @RequestParam String using) {
        boolean b = sysFreezeIpService.updateUsing(id, using);
        return ResultVOUtil.success(b);
    }

    @ApiOperation(value = "根据id删除登录设置", notes = "删除操作")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @Transactional
    @GetMapping(value = "delete")
    @OperationLog(value = "系统-删除登录设置")
    public ResultVO<Boolean> delete(@RequestParam String id) {
        boolean b = sysFreezeIpService.delete(id);
        return ResultVOUtil.success(b);
    }

    @ApiOperation(value = "保存登录设置", notes = "保存登录设置")
    @ApiImplicitParam(name = "sysFreezeIp", value = "SysFreezeIp类",  dataType = "SysFreezeIp")
    @OperationLog(value = "系统-保存登录设置")
    @Transactional
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody SysFreezeIp sysFreezeIp) {
        String str="^((25[0-5]|2[0-4]\\d|[1]{1}\\d{1}\\d{1}|[1-9]{1}\\d{1}|\\d{1})($|(?!\\.$)\\.)){4}$";
        if("".equals(sysFreezeIp.getIp()) || sysFreezeIp.getIp() == null || sysFreezeIp.getIp().matches(str)){
            if(sysFreezeIp.getId()== null || "".equals(sysFreezeIp.getId())) {
                sysFreezeIpService.save(sysFreezeIp);
            } else {
                sysFreezeIpService.update(sysFreezeIp);
            }
            return  ResultVOUtil.success();
        }
        return ResultVOUtil.error(1,"请输入正确是的IP！！");
    }
    
    @ApiOperation(value = "添加/编辑ip地址查重")
    @OperationLog(value = "系统-保添加/编辑ip地址查重")
    @Transactional
    @GetMapping(value = "checkIp")    
    public ResultVO<String> checkIp(@ApiParam(value="id")@RequestParam(required = false) String id , @ApiParam("IP地址")@RequestParam String ip) {
    	return sysFreezeIpService.checkIp(id, ip);
    }
}

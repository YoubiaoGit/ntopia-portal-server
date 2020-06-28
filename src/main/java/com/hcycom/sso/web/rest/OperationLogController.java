package com.hcycom.sso.web.rest;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hcycom.sso.domain.OperationLog;
import com.hcycom.sso.service.permissions.OperationLogService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: OperationLogController
 * @Description: TODO
 * @auther: lr
 * @date: 2018/6/27 21:10
 * @version: 1.0
 */
@Api(tags = {"操作日志接口"})
@Slf4j
@RestController
@RequestMapping("/api/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * @Title: findAll
     * @Description: TODO
     * @Author: zh
     * @Date: 21:20 2018/6/27
     * @Param: [keyWord, pageNum, pageSize]
     * @return: com.hcycom.mc.log.vo.ResultVO<java.util.List < com.hcycom.mc.log.domain.OperationLog>>
     */
    @ApiOperation("查看操作日志列表")
    @com.hcycom.sso.OperationLog.OperationLog(value = "系统监控-操作日志列表")
    @CrossOrigin
    @GetMapping("/findAll")
    public ResultVO<List<OperationLog>> findAll(
        @RequestParam(required = false) @ApiParam(value = "查询关键词") String keyWord,
        @RequestParam(required = false) @ApiParam("开始时间") String startTime,
        @RequestParam(required = false) @ApiParam("结束时间") String endTime,
        @RequestParam(required = false, defaultValue = "0") @ApiParam(value = "页码") int pageNum,
        @RequestParam(required = false, defaultValue = "0") @ApiParam(value = "每页条数") int pageSize) {
        log.info("[调用controller开始，入参：{}]", keyWord);
        try {
            if (pageNum == 0 && pageSize == 0) {
                List<OperationLog> list = operationLogService.findAll(keyWord, startTime, endTime);
                log.info("[调用controller结束，出参：{}]", JSON.toJSON(list));
                return ResultVOUtil.success(list);
            } else {
                PageInfo<OperationLog> pageInfo = operationLogService.findAllByPage(keyWord, pageNum, pageSize, startTime, endTime);
                log.info("[调用controller结束，出参：{}]", JSON.toJSON(pageInfo));
                return ResultVOUtil.success(pageInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }
    }

    /**
     * @Title: findOne
     * @Description: TODO
     * @Author: zh
     * @Date: 21:23 2018/6/27
     * @Param: [id]
     * @return: com.hcycom.mc.log.vo.ResultVO<com.hcycom.mc.log.domain.OperationLog>
     */
    @ApiOperation("操作日志详情")
    @com.hcycom.sso.OperationLog.OperationLog(value = "系统监控-操作日志详情")
    @CrossOrigin
    @GetMapping("/findOne")
    public ResultVO<OperationLog> findOne(@RequestParam @ApiParam("日志id") String id) {
        log.info("[调用controller开始，入参：{}]", id);
        try {
            OperationLog operationLog = operationLogService.findOne(id);
            log.info("[调用controller结束，出参：{}]", JSON.toJSON(operationLog));
            return ResultVOUtil.success(operationLog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "服务器异常...");
        }

    }

}

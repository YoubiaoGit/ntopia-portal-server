package com.hcycom.sso.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.hcycom.sso.OperationLog.OperationLog;
import com.hcycom.sso.domain.SysOrgan;
import com.hcycom.sso.dto.OrganRelationDTO;
import com.hcycom.sso.service.permissions.SysOrganService;
import com.hcycom.sso.service.permissions.UserService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 机构
 *
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 17:35
 */
@Api(tags = {"系统机构管理接口"}, description = "SysOrganController")
@RestController
@RequestMapping("/api/permissions/organ")
public class SysOrganController {
    @Autowired
    private SysOrganService sysOrganService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询机构信息", notes = "实现模糊查询")
    @ApiImplicitParam(name = "keyWord", value = "关键字", required = false, dataType = "String")
    @OperationLog(value = "系统-机构管理列表")
    @GetMapping(value = "find")
    public ResultVO<List<OrganRelationDTO>> finAllOrg(String keyWord) {
        List<OrganRelationDTO> organDTOList = sysOrganService.findAll(keyWord);
//        List<OrganRelationDTO> childrenList = new ArrayList<>();
//        //根据当前用户查询组织机构
//        UserInfoVO user = this.sysUserService.getRedisUser();
//        String organId = user.getOrganId();
//        String parentId = getParentOrgan(organId);
//        if(!StringUtils.isEmpty(parentId)){
//            for (OrganRelationDTO organRelationDTO:organDTOList){
//                if(organRelationDTO.getId().equals(parentId)){
//                    childrenList.add(organRelationDTO);
//                    break;
//                }
//            }
//        }

        return ResultVOUtil.success(organDTOList);
    }
//    private  String getParentOrgan(String id){
//        OrganRelationDTO organ = sysOrganService.findOrganById(id);
//        if(!StringUtils.isEmpty(organ)){
//            String organId = getParentOrgan(organ.getParentId());
//            if ("0".equals(organId)) {
//                return id;
//            }
//            return organId;
//        }else {
//            return id;
//        }
//    }


    //根据id查询
    @ApiOperation(value = "查询机构", notes = "根据id获取机构信息")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    @GetMapping(value = "findById")
    public ResultVO<JSONObject> findById(@RequestParam String id) {
        JSONObject json = new JSONObject();
        OrganRelationDTO organ = sysOrganService.findById(id);//当前机构
        json.put("organ", organ);
        return ResultVOUtil.success(json);
    }

    @ApiOperation(value = "保存机构数据", notes = "添加/修改机构信息")
    @ApiImplicitParam(name = "organ", value = "SysOrgan类", dataType = "SysOrgan")
    @OperationLog(value = "系统-保存机构数据")
    @Transactional
    @PostMapping(value = "save")
    public ResultVO<String> save(@RequestBody @Valid SysOrgan organ) {
        if (StringUtils.isEmpty(organ.getId())) {
            //添加
            sysOrganService.insertOrgan(organ);
        } else {
            //更新
            sysOrganService.updateOrgan(organ);
        }
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "删除机构", notes = "删除指定机构")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @Transactional
    @GetMapping(value = "delete")
    public ResultVO<String> delete(@RequestParam String id) {
        sysOrganService.deleteOrgan(id);
        return ResultVOUtil.success();

    }


    //获取主负责人
    @ApiOperation(value = "获取主负责人", notes = "获取主负责人")
    @GetMapping(value = "findMainHead")
    public ResultVO<String> findMainHead() {
        List<Map<String, Object>> userList = userService.mainHead();//主要人员
        return ResultVOUtil.success(userList);
    }

    //根据组织机构查询区域
    @ApiOperation(value = "根据组织机构查询区域", notes = "根据组织机构查询区域")
    @ApiImplicitParam(name = "oid", value = "机构id", dataType = "String")
    @GetMapping(value = "findAreaByOrgan")
    public ResultVO<String> findAreaByOrgan(String oid) {
        SysOrgan sysOrgan = sysOrganService.findAreaByOrgan(oid);
        return ResultVOUtil.success(sysOrgan);
    }

    //根据多个id查询机构
    @ApiOperation(value = "根据多个id查询机构", notes = "根据多个id查询机构")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "String")
    @GetMapping(value = "findByOrganIds")
    public ResultVO<String> findByIds(String ids) {
        List<OrganRelationDTO> organ = sysOrganService.findByIds(ids);
        return ResultVOUtil.success(organ);
    }

    //根据机构id查询该机构下的人员信息
    @ApiOperation(value = "根据机构id查询用户", notes = "根据多个id查询机构")
    @GetMapping(value = "findUserByOrganId")
    public ResultVO<String> findUsersByOrganId(@ApiParam("机构id") @RequestParam String id) {
        List<String> userInfo = sysOrganService.findUsersByOrganId(id);
        return ResultVOUtil.success(userInfo);
    }
}

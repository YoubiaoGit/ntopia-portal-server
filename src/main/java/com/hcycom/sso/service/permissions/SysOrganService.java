package com.hcycom.sso.service.permissions;

import com.hcycom.sso.domain.SysOrgan;
import com.hcycom.sso.dto.*;

import java.util.List;

public interface SysOrganService {
    List<OrganRelationDTO> findAll(String keyWords);

    //查询机构
    OrganRelationDTO  findOrganById(String id);

    OrganRelationDTO findById(String id);
    //查询所有公司
    List<CompanyDTO> findAllCompany();
    //查询所有部门
    List<DepartmentDTO> findAllDepartment(String parentId);
    boolean deleteOrgan(String id);

    boolean insertOrgan(SysOrgan organ);

    boolean updateOrgan(SysOrgan organ);

    SysOrgan findAreaByOrgan(String oid);
    //根据多个id查机构列表
    List<OrganRelationDTO> findByIds(String ids);
    
    //根据机构id查询用户
    List<String> findUsersByOrganId(String id);
}

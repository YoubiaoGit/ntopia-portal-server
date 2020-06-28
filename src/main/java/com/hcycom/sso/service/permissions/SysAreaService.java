package com.hcycom.sso.service.permissions;

import com.hcycom.sso.domain.SysArea;

import java.util.List;

public interface SysAreaService {
    List<SysArea> findAll(String keywords);
    List<String> findAreaType();
    SysArea findById(String id);
    List<SysArea> findAllTopArea();
    List<SysArea> childArea(String code);
    boolean deleteArea(String id);
    boolean insertArea(SysArea area);
    boolean updateArea(SysArea area);
    SysArea findByOrgan(String organId);
}

package com.hcycom.sso.service.permissions;


import com.hcycom.sso.domain.SysSpecialty;

import java.util.List;

public interface SysSpecialtyService {
    List<SysSpecialty> findAll();
    SysSpecialty findOne(String id);
}

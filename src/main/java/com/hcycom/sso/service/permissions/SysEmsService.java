package com.hcycom.sso.service.permissions;


import com.hcycom.sso.domain.SysEms;

import java.util.List;

public interface SysEmsService {
    List<SysEms> findAll();
    SysEms findByOne(String id);
}

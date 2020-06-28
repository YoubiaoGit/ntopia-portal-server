package com.hcycom.sso.service.permissions;

import com.hcycom.sso.domain.OnlineUser;

import java.util.List;
import java.util.Map;

public interface OnlineUserService {
    List<OnlineUser> findAll(String keyWord, Map<String,String> map);
}

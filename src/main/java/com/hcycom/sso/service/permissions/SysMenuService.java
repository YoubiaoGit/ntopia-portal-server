package com.hcycom.sso.service.permissions;

import com.hcycom.sso.domain.SysMenu;
import com.hcycom.sso.dto.MenuDTO;
import com.hcycom.sso.vo.ResultVO;

import java.util.List;
import java.util.Map;

public interface SysMenuService {
    List<SysMenu> findParentMenu(List<String> roleids);
    Map<String,Object> findMenuByPid(String pid,List<String> roleids);
   // List<SysMenu> findAllMenu();
    List<SysMenu> findByRoleId(String roleid);
    boolean deleteMenu(String id);
    SysMenu findById(String id);

    List<SysMenu> findAll();
    boolean insertMenu(SysMenu menu);

    boolean updateMenu(SysMenu menu);

    List<SysMenu> findChildMenu();

    MenuDTO findParent (String id);
    
    
	ResultVO<String> checkRepeat(String id, String pid, String name);

}

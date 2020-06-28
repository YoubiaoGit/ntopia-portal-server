package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.cache.SysMenuCache;
import com.hcycom.sso.dao.permissions.SysMenuDao;
import com.hcycom.sso.domain.SysMenu;
import com.hcycom.sso.dto.MenuDTO;
import com.hcycom.sso.exception.SsoException;
import com.hcycom.sso.service.permissions.SysMenuService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.utils.UUIDTool;
import com.hcycom.sso.vo.ResultVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    final static Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);
    @Autowired
    private SysMenuDao sysMenuDao;

    @Override
    public List<SysMenu> findParentMenu(List<String> roleids) {
        List<SysMenu> sysMenus = sysMenuDao.findParentMenu(roleids);
        List<SysMenu> topAll= new ArrayList<>();
        sysMenus.forEach((menu) -> {
            String id  = menu.getId();
            //根据id查出它的最顶层的父元素
            List<SysMenu> sysMenuList = sysMenuDao.findParentById(id);
            for (SysMenu sysMenu : sysMenuList) {
                if(sysMenu!=null&&sysMenu.getParentId().equals("1")){
                    topAll.add(sysMenu);
                }
            }
        });
        //去重list中的对象
        List<SysMenu> unique = topAll.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(comparing(SysMenu::getId))), ArrayList::new)
        );
        //根据排序字段排序
        Collections.sort(unique, Comparator.comparing(SysMenu::getSort));
        return unique;
    }
    //左菜单列表
    @Override
    public Map<String,Object> findMenuByPid(String pid,List<String> roleids) {
        List<SysMenu> allList = sysMenuDao.findParentMenu(roleids);//先获取所有菜单
        List<SysMenu> clist = new ArrayList<>(200);
        List<SysMenu> unique = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        SysMenu sysMenu =  findById(pid);
        String pname = sysMenu.getName();
        if(allList != null) {
            allList.forEach((m) -> {
                //根据id获取用户权限所拥有的父id
                List<SysMenu> sysMenuList = sysMenuDao.findParentById(m.getId());
                for (SysMenu sysMenu1 : sysMenuList) {
                    if(sysMenu1!=null){
                        if (sysMenu1.getParentId().equals(pid)) {
                            clist.add(sysMenu1);
                        }
                    }
                }
            });
            //去重clist中的对象
            unique = clist.stream().collect(
                    collectingAndThen(
                            toCollection(() -> new TreeSet<>(comparing(SysMenu::getId))), ArrayList::new)
            );
            //根据排序字段排序
            Collections.sort(unique, Comparator.comparing(SysMenu::getSort));
            map.put("sysMenu",unique);

            String twoName = unique.get(0).getName();
            String newurl = pname+"/"+twoName;
            map.put("url",newurl);
        }
        findChildList(unique, allList);
        return map;
    }
    @Override
    public List<SysMenu> findByRoleId(String roleid) {
        if(StringUtils.isEmpty(roleid)) {
            return findAll();
        }
        List<SysMenu> allList = sysMenuDao.findAllMenu();//先获取所有菜单
        List<SysMenu> rmList = sysMenuDao.findByRoleId(roleid);
        List<SysMenu> plist = new ArrayList<>(200);
        for (SysMenu menu : allList) {
            for (SysMenu rMenu : rmList) {
                if(rMenu.getId().equals(menu.getId())) {
                    menu.setChecked(true);
                    break;
                }
            }
        }

        if(allList != null && allList.size() > 0) {
            for(SysMenu m : allList) {
                if(m.getParentId() != null && "0".equals(m.getParentId())) {
                    plist.add(m);
                }
            }
        }
        findChildList(plist, allList);

        return plist;
    }
    public void findChildList(List<SysMenu> clist, List<SysMenu> allList) {
        clist.forEach((pm) -> {
            pm.setChildren(getChild(pm.getId(), allList));
        });
    }

    @Override
    @CacheEvict(value = {SysMenuCache.CACHE_ALL_MENU, SysMenuCache.CACHE_MENU_ONE, SysMenuCache.CACHE_CHILD_MENU, SysMenuCache.CACHE_MAIN_MENU}, allEntries = true)
    public boolean deleteMenu(String id) {
        List<String> list = sysMenuDao.findAllPid();
        if(list != null && list.contains(id.toString())) {
            throw new SsoException("该菜单存在子菜单, 无法删除！");
        }
        //int i = sysMenuDao.deleteByPrimaryKey(id);
        int i = 0;
       try {
            i = sysMenuDao.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new SsoException("该菜单被角色关联，无法删除！");
        }
        return i > 0;
    }
//    @Cacheable(value = SysMenuCache.CACHE_MENU_ONE)
    @Override
    public SysMenu findById(String id) {
        return sysMenuDao.selectByPrimaryKey(id);
    }

    @Override
    @CacheEvict(value = {SysMenuCache.CACHE_ALL_MENU, SysMenuCache.CACHE_MENU_ONE, SysMenuCache.CACHE_CHILD_MENU, SysMenuCache.CACHE_MAIN_MENU}, allEntries = true)
    public boolean insertMenu(SysMenu menu) {
        logger.info("menu-->"+menu);
        String uuid = UUIDTool.getUUID();
        menu.setId(uuid);
        if(menu.getSort() == null) {
            menu.setSort(100);
        }
        menu.setUpdateTime(new Date());
//        menu.setAuthFlag("");
        //SysUser redisUser = sysUserService.getRedisUser();
       // menu.setCreateUser(redisUser == null ? "": redisUser.getName());
        return sysMenuDao.insertSelective(menu) > 0;
    }

    @Override
    @CacheEvict(value = {SysMenuCache.CACHE_ALL_MENU, SysMenuCache.CACHE_MENU_ONE, SysMenuCache.CACHE_CHILD_MENU, SysMenuCache.CACHE_MAIN_MENU}, allEntries = true)
    public boolean updateMenu(SysMenu menu) {
        if(menu.getSort() == null) {
            menu.setSort(100);
        }
        int i = sysMenuDao.updateByPrimaryKeySelective(menu);
        
        //判断是显示还是隐藏
        if(menu.getIsDisplay().equals("0")) {
        	//查找他的子菜单全部改为隐藏
        	if(!hideChildrenMenu(menu.getId())) {
        		return false;
        	}
        }else {
        	//查找他的父菜单全部改为显示
        	if(!showParentMenu(menu.getParentId())) {
        		return false;
        	}
        }
        
        return i > 0;
    }
    
    public boolean hideChildrenMenu(String id) {
    	List<String> child = new ArrayList<>();
    	boolean check = true;
    	//先获取下一级菜单的id
    	child = sysMenuDao.getChildren(id);
    	//如果存在下一级菜单
    	if(child.size()!=0) {
    		//循环遍历是否下一级菜单还存在菜单
    		for (String cid : child) {
    			//更改菜单状态为显示
    			check = sysMenuDao.hideMenu(cid);
    			check = hideChildrenMenu(cid);
    		}
    	}
    	return check;
    }
    
    public boolean showParentMenu(String id) {
    	boolean check = true;
    	//获取父菜单id
    	String parentId = sysMenuDao.getParentId(id);
    	//如果父菜单id不为空，则修改父菜单id为显示
    	if(!StringUtils.isEmpty(id)) {
    		check = sysMenuDao.showMenu(id);
    		check = showParentMenu(parentId);
    	}
    	return check;
    }

    @Override
    public List<SysMenu> findChildMenu() {
        return sysMenuDao.findChildMenu();
    }

    //根据id查询父元素
    @Override
    public MenuDTO findParent(String id) {
        return sysMenuDao.findByParent(id);
    }

    public List<SysMenu> getChild(String id, List<SysMenu> rootMenu) {
        // 子菜单
        List<SysMenu> childList = new ArrayList<>();

        rootMenu.forEach((menu) -> {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (id.equals(menu.getParentId())) {
                childList.add(menu);
            }
        });

        // 把子菜单的子菜单再循环一遍

        childList.forEach((menu) -> {
            menu.setChildren(getChild(menu.getId(), rootMenu));
        });
        // 递归退出条件
        if (childList.size() == 0) {
            return childList;
        }
        return childList;
    }

    @Override
//    @Cacheable(value = SysMenuCache.CACHE_ALL_MENU)
    public List<SysMenu> findAll() {
        List<SysMenu> allList = sysMenuDao.findAllMenu();//先获取所有菜单

        //组装指定pid的菜单
        List<SysMenu> plist = new ArrayList<>(200);
            if(allList != null && allList.size() > 0) {
                for(SysMenu m : allList) {
                    if("1".equals(m.getIsDisplay())){
                        m.setDisplayName("显示");
                    }else{
                        m.setDisplayName("隐藏");
                    }
                    if(m.getParentId() != null && "0".equals(m.getParentId())) {
                        plist.add(m);
                    }
            }

            /*
               这里用lambda表达式会报错,
               原因:从lambda 表达式引用的本地变量必须是最终变量或实际上的最终变量
            allList.forEach((m) -> {
                if(m.getPid() != null && m.getPid() == 0) {
                   plist.add(m);
                }
            });*/

        } else {
            plist = allList;
        }
        findChildList(plist, allList);
        return plist;
    }
	@Override
	public ResultVO<String> checkRepeat(String id, String pid, String name) {

		boolean res = false;
		if(pid.equals("1")) {
			name = "/"+name;
		}
		if(StringUtils.isEmpty(id)) {
			res = sysMenuDao.checkSaveMenu(name, pid);
		}else {
			res = sysMenuDao.checkUpdateMenu(name, pid, id);
		}
		
		if(res) {
			return ResultVOUtil.error(1,"该路由名称已存在！");
		}else {
			return ResultVOUtil.success();
		}
	
	}

}

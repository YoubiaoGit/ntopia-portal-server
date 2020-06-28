package com.hcycom.sso.service.permissions.impl;

import com.hcycom.sso.cache.SysOrganCache;
import com.hcycom.sso.dao.permissions.SysOrganDao;
import com.hcycom.sso.dao.permissions.SysSpecialtyDao;
import com.hcycom.sso.dao.permissions.SysSpecialtyOrganDao;
import com.hcycom.sso.domain.SysOrgan;
import com.hcycom.sso.domain.SysSpecialty;
import com.hcycom.sso.domain.SysSpecialtyOrgan;
import com.hcycom.sso.dto.CompanyDTO;
import com.hcycom.sso.dto.DepartmentDTO;
import com.hcycom.sso.dto.OrganRelationDTO;
import com.hcycom.sso.dto.SuperOrgan;
import com.hcycom.sso.exception.SsoException;
import com.hcycom.sso.service.SysUserService;
import com.hcycom.sso.service.permissions.SysOrganService;
import com.hcycom.sso.utils.UUIDTool;
import com.hcycom.sso.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service
public class SysOrganServiceImpl implements SysOrganService {
    @Autowired
    private SysOrganDao sysOrganDao;

    @Autowired
    private SysSpecialtyOrganDao sysSpecialtyOrganDao;
    @Autowired
    private SysSpecialtyDao sysSpecialtyDao;

    @Autowired
    private SysOrganService sysOrganService;
    
    @Autowired
    private SysUserService sysUserService;

    @Override
//    @Cacheable(value = SysOrganCache.CACHE_ALL_ORGAN)
    public List<OrganRelationDTO> findAll(String search) {
    	//查询获取存在用户的机构信息
        List<OrganRelationDTO> userInfo = sysUserService.getUserOrganInfo();
        Map<String,Integer> userInfoMap = new HashMap<>();
        userInfo.forEach(obj ->{
        	userInfoMap.put(obj.getId(), obj.getUserCount());
        });
        
        List<OrganRelationDTO> allList = sysOrganDao.findAll(search);
        allList.forEach(organ ->{
        	if(userInfoMap.containsKey(organ.getId())) {
        		organ.setUserCount(userInfoMap.get(organ.getId()));
        	}
        });
        
      /*  Set<Entry<String, String>> entrySet = userInfo.entrySet();
        for (Entry<String, String> entry : entrySet) {
            allList.forEach(organ ->{
            	if(organ.getId().equals(entry)) {
            		organ.setUserCount(entry.getValue());
            	}
            });
        }*/

/*        allList.forEach(organ ->{
        	if(organ.getId().equals())
        });*/
 
        List<OrganRelationDTO> list = new ArrayList<>(200);
        if(StringUtils.isEmpty(search)) {
            for (OrganRelationDTO o: allList) {
                if("0".equals(o.getParentId())) {
                    list.add(o);
                }
            }
        } else {
            list = allList;
        }
        if(list.size() > 0) {
            list.forEach(o2 -> {
                o2.setChildren(getChild(o2.getId(), allList));
            });
        }
        //System.out.println("执行次数 index : " + index);
        index = 0;
//        List<OrganRelationDTO> childrenList = new ArrayList<>();
//        //根据当前用户查询组织机构
//        UserInfoVO user = this.sysUserService.getRedisUser();
//        String organId = user.getOrganId();
//        String parentId = getParentOrgan(organId);
//        if(!StringUtils.isEmpty(parentId)){
//            for (OrganRelationDTO organRelationDTO:allList){
//                if(organRelationDTO.getId().equals(parentId)){
//                    childrenList.add(organRelationDTO);
//                    break;
//                }
//            }
//        }
        return list;
    }

    @Override
    public OrganRelationDTO findOrganById(String id) {
        return sysOrganDao.findOrganById(id);
    }
    @Override
//    @Cacheable(value = SysOrganCache.CACHE_ORGAN_ONE)
    public OrganRelationDTO findById(String id) {
        OrganRelationDTO o = sysOrganDao.findById(id);
        if("1".equals(id)) {
            SysOrgan organ = sysOrganDao.selectByPrimaryKey(id);
            o.setParentId("0");
            o.setPname(organ.getName());
        }
        return o;
    }
    @Override
    public List<DepartmentDTO> findAllDepartment(String parentId) {
        List<DepartmentDTO> dpList = sysOrganDao.findAllDepartMent(parentId);
//        List<DepartmentDTO> list = getTreeData(dpList);

        return dpList;
    }

    //封装树状结构
    private List getTreeData(List<? extends SuperOrgan> rlist) {

        List<SuperOrgan> list = new ArrayList<>(200);

        rlist.forEach(cp -> {
            if("0".equals(cp.getParentId())) {
                list.add(cp);
            }
        });

        if(list.size() > 0) {
            list.forEach(cp2 -> {
                cp2.setChildren(getChild(cp2.getId(), rlist));
            });
        }

        return list;
    }

    @Override
    public List<CompanyDTO> findAllCompany() {
        List<CompanyDTO> cpList = sysOrganDao.findAllCompany();

        List<CompanyDTO> list = getTreeData(cpList);

        return list;
    }

    @Override
    @CacheEvict(value = {SysOrganCache.CACHE_ALL_ORGAN, SysOrganCache.CACHE_ORGAN_ONE}, allEntries = true)
    public boolean deleteOrgan(String id) {
        List<String> list = sysOrganDao.findAllpid();
        if(list != null && list.contains(id.toString())) {
            throw new SsoException("该机构下有子机构, 不能随便删除");
        }
        int i = sysOrganDao.deleteByPrimaryKey(id);
        //int j = sysSpecialtyOrganDao.delete(id);
       /* try {
            i = sysOrganDao.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new SsoException("该机构关联其他数据, 不能删除");
        }*/
        return i > 0;
    }

    @Override
    @CacheEvict(value = {SysOrganCache.CACHE_ALL_ORGAN, SysOrganCache.CACHE_ORGAN_ONE}, allEntries = true)
    public boolean updateOrgan(SysOrgan organ) {
        organ.setUpdateTime(new Date());
        int i =  sysOrganDao.updateByPrimaryKeySelective(organ);
        List<SysSpecialty> list = sysSpecialtyDao.findAll();
        for (SysSpecialty sysSpecialty: list) {
            if(sysSpecialty.getName().equals(organ.getName())) {
                String specialtyId  = sysSpecialty.getId();
                SysSpecialtyOrgan sysSpecialtyOrgan = new SysSpecialtyOrgan();
                sysSpecialtyOrgan.setSpecialtyId(specialtyId);
                sysSpecialtyOrgan.setOrganId(organ.getId());
                sysSpecialtyOrganDao.update(sysSpecialtyOrgan);
                break;
            }
        }
        return i>0;
    }

    @Override
    //根据组织机构查询区域
    public SysOrgan findAreaByOrgan(String oid) {
        return sysOrganDao.findAreaByOrgan(oid);
    }

    @Override
    //根据多个id查询机构
    public List<OrganRelationDTO> findByIds(String ids) {
        List<OrganRelationDTO>  organList = new ArrayList<>();
        List<String> listString = Arrays.asList(ids.split(","));
        List<OrganRelationDTO> allList = sysOrganDao.findAll(null);//先获取所有机构
        List<OrganRelationDTO> plist = new ArrayList<>();
        for (String string : listString) {
            OrganRelationDTO organ = sysOrganDao.findById(string);
            if(organ!=null){
                organ.setChecked(true);
                organList.add(organ);
            }
        }
        for (OrganRelationDTO organ : allList) {
            for (OrganRelationDTO organ2 : organList) {
                if(organ2.getId().equals(organ.getId())) {
                    organ.setChecked(true);
                    break;
                }
            }
        }
        if(allList != null && allList.size() > 0) {
            for(OrganRelationDTO O : allList) {
                if(O.getParentId() != null && "0".equals(O.getParentId())) {
                    plist.add(O);
                }
            }
        }
        findChildList(plist, allList);
        return plist;
    }
    private  String getParentOrgan(String id){
        OrganRelationDTO organ = sysOrganService.findOrganById(id);
        if(!StringUtils.isEmpty(organ)){
            String organId = getParentOrgan(organ.getParentId());
            if ("0".equals(organId)) {
                return id;
            }
            return organId;
        }else {
            return id;
        }
    }
    public void findChildList(List<OrganRelationDTO> clist, List<OrganRelationDTO> allList) {
        clist.forEach((pm) -> {
            pm.setChildren(getChild(pm.getId(), allList));
        });
    }
    @Override
    @CacheEvict(value = {SysOrganCache.CACHE_ALL_ORGAN, SysOrganCache.CACHE_ORGAN_ONE}, allEntries = true)
    public boolean insertOrgan(SysOrgan organ) {
        String uuid = UUIDTool.getUUID();
        organ.setId(uuid);
        organ.setUpdateTime(new Date());
        if(organ.getSort() == null) {
            organ.setSort(100);
        }
        int i =  sysOrganDao.insertSelective(organ);
        List<SysSpecialty> list = sysSpecialtyDao.findAll();
        for (SysSpecialty sysSpecialty: list) {
            if(sysSpecialty.getName().equals(organ.getName())) {
                String specialtyId  = sysSpecialty.getId();
                SysSpecialtyOrgan sysSpecialtyOrgan = new SysSpecialtyOrgan();
                sysSpecialtyOrgan.setOrganId(uuid);
                sysSpecialtyOrgan.setSpecialtyId(specialtyId);
                sysSpecialtyOrganDao.insert(sysSpecialtyOrgan);
                break;
            }
        }
        return i>0;
    }

    private  int index = 0;

    //递归查询
    private   List<SuperOrgan> getChild(String id, List<? extends SuperOrgan> rootOrg) {
//        index++;
        // 子机构
        List<SuperOrgan> childList = new ArrayList<>();
        rootOrg.forEach(org -> {
            // 遍历所有节点，将父机构id与传过来的id比较
            if (org.getParentId().equals(id)) {
                childList.add(org);
            }
        });


        // 把子机构下的机构再循环一遍

        childList.forEach(org -> {
            org.setChildren(getChild(org.getId(), rootOrg));
        });
        // 递归退出条件
        if (childList.size() == 0) {
            return childList;
        }
        return childList;
    }

	@Override
	public List<String> findUsersByOrganId(String id) {
		return sysSpecialtyOrganDao.findUsersByOrganId(id);
	}

}

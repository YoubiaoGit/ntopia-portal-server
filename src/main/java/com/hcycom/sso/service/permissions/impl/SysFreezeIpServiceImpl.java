package com.hcycom.sso.service.permissions.impl;

import com.github.pagehelper.PageInfo;
import com.hcycom.sso.cache.SysLoginIpCache;
import com.hcycom.sso.dao.permissions.SysFreezeIpDao;
import com.hcycom.sso.domain.SysFreezeIp;
import com.hcycom.sso.exception.SsoException;
import com.hcycom.sso.service.BaseFindPageService;
import com.hcycom.sso.service.permissions.SysFreezeIpService;
import com.hcycom.sso.utils.ResultVOUtil;
import com.hcycom.sso.utils.UUIDTool;
import com.hcycom.sso.vo.ResultVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-04-19 17:46
 */
@Service
public class SysFreezeIpServiceImpl extends BaseFindPageService<SysFreezeIp> implements SysFreezeIpService {

    @Autowired
    private SysFreezeIpDao sysFreezeIpDao;
    @Override
//    @Cacheable(value = SysLoginIpCache.CACHE_FIND_IP_PAGE)
    public PageInfo<SysFreezeIp> findByPage(String search, int start, int pageSize) {
        return super.findByPage(search, start, pageSize, sysFreezeIpDao);
    }

    @Override
    @CacheEvict(value = SysLoginIpCache.CACHE_FIND_IP_PAGE, allEntries = true)
    public boolean updateUsing(String id, String using) {
        return sysFreezeIpDao.updateUsing(id, using) > 0;
    }

    @Override
    @CacheEvict(value = SysLoginIpCache.CACHE_FIND_IP_PAGE, allEntries = true)
    public boolean update(SysFreezeIp sysFreezeIp) {
        if(StringUtils.isEmpty(sysFreezeIp.getId())) {
            throw new SsoException("id不能为空");
        }
        return sysFreezeIpDao.updateByPrimaryKeySelective(sysFreezeIp) > 0;
    }

    @Override
    @CacheEvict(value = SysLoginIpCache.CACHE_FIND_IP_PAGE, allEntries = true)
    public boolean delete(String id) {
        return sysFreezeIpDao.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean save(SysFreezeIp sysFreezeIp) {
        String uuid = UUIDTool.getUUID();
        sysFreezeIp.setId(uuid);
        return sysFreezeIpDao.insertLoginIp(sysFreezeIp)>0;
    }

    @Override
    public SysFreezeIp findBy(String ip) {
        return sysFreezeIpDao.findByIp(ip);
    }

	@Override
	public ResultVO<String> checkIp(String id, String ip) {
		boolean res = false;
		if(StringUtils.isEmpty(id)) {
			res = sysFreezeIpDao.checkSaveIp(ip);
		}else {
			res = sysFreezeIpDao.checkUpdatIp(id,ip);
		}
		
		if(res) {
			return ResultVOUtil.error(1,"该IP已存在");
		}else {
			return ResultVOUtil.success();
		}
	}
}

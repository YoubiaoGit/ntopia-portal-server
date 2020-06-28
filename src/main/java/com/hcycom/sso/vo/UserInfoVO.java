package com.hcycom.sso.vo;

import com.hcycom.sso.domain.SysUser;
import com.hcycom.sso.dto.MenuDTO;
import lombok.Data;

import java.util.List;

/**
 * VO=View Object
 * 返回给视图的用户信息实体类。
 *
 * @author Silence
 * @create 2019-01-10 17:17
 */
@Data
public class UserInfoVO {

    /** 用户ID **/
    private String id;

    /** 登录名 **/
    private String loginName;

    /** 用户姓名 **/
    private String fullName;

    /** 手机号码 **/
    private String telephone;

    /** 邮箱 **/
    private String email;

    /** 头像 **/
    private String pic;

   /** 专业 **/
    private String specialty;

    /** 省 **/
    private String province;

    /** 城市id **/
    private String cityId;

    /** 城市名称 **/
    private String cityName;

    /** 机构级别 **/
//    private String organGrade;

    /** 机构id **/
    private String organId;

    /** 公司名称 **/
    private String company ;

    /** 数据范围 **/
//    private List<String> dataScope ;

   /** 用户角色**/
    private List<String> roleids;

	public UserInfoVO(SysUser sysUser) {
		super();
		this.id = sysUser.getId();
		this.loginName = sysUser.getLoginName();
		this.fullName = sysUser.getFullName();
		this.telephone = sysUser.getTelephone();
		this.email = sysUser.getEmail();
		this.pic = sysUser.getPic();
		//this.specialty = sysUser.getspecialty;
		//this.province = sysUser.getprovince;
		this.cityId = sysUser.getCityId();
		this.cityName = sysUser.getCityName();
		this.organId = sysUser.getOrganId();
		//this.company = sysUser.getcompany;
		this.roleids = sysUser.getRoleids();
	}

	public UserInfoVO() {
		super();
	}
    
    
    

}

package com.hcycom.sso.utils;

import com.hcycom.sso.domain.SysArea;
import com.hcycom.sso.exception.SsoException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String dateToString(Date date) {
		return sdf.format(date);
	}

	public static Date stringToDate(String date) {
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			//e.printStackTrace();
			throw  new SsoException("日期转换错误!!");
		}
	}



	public static String getProjectName(HttpServletRequest request, String requrl) {
		String[] s = requrl.split("//");
		
		String name = s[1].substring((request.getServerName()+":" +request.getLocalPort()+"/").length());
		
		System.out.println(request.getServerName()+":" +request.getLocalPort());
		//System.out.println(name);
		
		return name.split("/")[0];
	}

	public  static  List<String>  idLists(String id, List<String> ids) {
		List<String> list = new ArrayList<>();
		list.add(id);//主键id
		for (String str : ids) {
			list.add(str);
		}
		return list;
	}

	public static void main(String[] args) {
	}
	/**
	 * token需要在url地址中过滤掉,防止用户拼参数, 只从cookie中拿参数
	 * @param requstUrl 请求的地址
	 * @param queryurl 地址后的参数
	 * @return
	 */
	public static String checkUrl(String requstUrl, String queryurl) {
		StringBuffer sb = new StringBuffer(requstUrl);
		
		String[] s = queryurl.split("&");
		Map<String,String> param = new LinkedHashMap<>();
		for (String str : s) {
			String[] strArr = str.split("=");
			if(strArr != null && strArr.length > 1) {
				param.put(strArr[0], strArr[1]);
			}
		}
		if(param.size() > 0) {
			if(sb.indexOf("?") > 0) {
				sb.append("&");
			} else {
				sb.append("?");
			}
			
			for (String en : param.keySet()) {
				if(!"token".equals(en)) {
					sb.append(en+"="+param.get(en)+"&");
				}
			}
			if(sb.lastIndexOf("&") > 0) {
				String surl = sb.substring(0, sb.length() - 1);
				return surl;
			}
			
		}
		return sb.toString();
	}

}

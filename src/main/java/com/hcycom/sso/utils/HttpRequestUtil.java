package com.hcycom.sso.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {
	/**
	 * 远程接口的调用
	 * @param url 接口地址
	 * @param param 需要的参数
	 * @return 返回 JSONObject对象,用code判断成功与否
	 */
	public static JSONObject postRequest(String url, Map<String, String> param) {
		PostMethod method = null;
		JSONObject json = null;
		try {
			for (Entry<String, String> entry : param.entrySet()) {
				method.addParameter(entry.getKey(), entry.getValue());
			}

			method = new PostMethod(url);
			HttpClient httpClient = new HttpClient();
			HttpConnectionManagerParams managerParams = httpClient
					   .getHttpConnectionManager().getParams();
			 // 设置连接超时时间(单位毫秒) 
			 managerParams.setConnectionTimeout(9000);
			 // 设置读数据超时时间(单位毫秒) 
			 managerParams.setSoTimeout(15000);
			int status = httpClient.executeMethod(method);
			if(status == HttpStatus.SC_OK) {
				String str = method.getResponseBodyAsString();
				json =JSONObject.parseObject(str);
				System.out.println("str -->" + str);
				return json;
			} else {
				throw new HttpException("连接出错或连接超时!!");
			}
			
		} catch (Exception e) {
			String s = JSON.toJSONString(ResultVOUtil.error(1, "请求出错"));
			json = JSONObject.parseObject(s);
			e.printStackTrace();
			return json;
		} finally {
			if(method != null) {
				method.releaseConnection();//释放连接
			}
			
		}
		
	}


	/*public static void main(String[] args) {
		new HttpRequestUtil().getCookie();
	}*/
	public static JSONObject getRequest(String url, Map<String, String> param) {
		GetMethod method = null;
		JSONObject json = null;
		try {
			StringBuffer sbUrl = new StringBuffer(url);
			if(sbUrl.indexOf("?") > 0) {
				sbUrl.append("&");
			} else {
				sbUrl.append("?");
			}
			for (Entry<String, String> entry : param.entrySet()) {
				sbUrl.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			url = sbUrl.toString();
			url = url.substring(0, sbUrl.length() - 1);

			method = new GetMethod(url);
			HttpClient httpClient = new HttpClient();
			HttpConnectionManagerParams managerParams = httpClient
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(9000);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(15000);
			int status = httpClient.executeMethod(method);
			if(status == HttpStatus.SC_OK) {
				String str = method.getResponseBodyAsString();
				json =JSONObject.parseObject(str);
				System.out.println("str -->" + str);
				return json;
			} else {
				throw new HttpException("连接出错或连接超时!!");
			}

		} catch (Exception e) {
			String s = JSON.toJSONString(ResultVOUtil.error(1, "请求出错"));
			json = JSONObject.parseObject(s);
			e.printStackTrace();
			return json;
		} finally {
			if(method != null) {
				method.releaseConnection();//释放连接
			}

		}

	}


	public void getCookie(){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get=new HttpGet("http://www.taobao.com");
		HttpClientContext context = HttpClientContext.create();
		try {
			CloseableHttpResponse response = httpClient.execute(get, context);
			try{
				System.out.println(">>>>>>cookies:");
				context.getCookieStore().getCookies().forEach(System.out::println);
			}
			finally {
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/**
	 * 获取浏览器版本信息
	 * @Title: getBrowserName
	 * @data:2015-1-12下午05:08:49
	 * @author:
	 * @param agent
	 * @return
	 */
	public static String getBrowserName(String agent) {
		if(agent.indexOf("msie 7")>0){
			return "ie7";
		}else if(agent.indexOf("msie 8")>0){
			return "ie8";
		}else if(agent.indexOf("msie 9")>0){
			return "ie9";
		}else if(agent.indexOf("msie 10")>0){
			return "ie10";
		}else if(agent.indexOf("msie")>0){
			return "ie";
		}else if(agent.indexOf("opera")>0){
			return "opera";
		}else if(agent.indexOf("firefox")>0){
			return "firefox";
		}else if(agent.indexOf("webkit")>0){
			return "webkit";
		}else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
			return "ie11";
		}else{
			return "Others";
		}
	}

}

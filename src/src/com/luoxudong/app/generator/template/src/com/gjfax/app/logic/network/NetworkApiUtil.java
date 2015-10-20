/**
 * Title: NetworkApiUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-30 下午4:39:47
 * Version 1.0
 */
package com.gjfax.app.logic.network;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.gjfax.app.logic.utils.GlobalUtil;
import com.gjfax.app.logic.utils.MetaDataUtil;

/** 
 * ClassName: NetworkApiUtil
 * Description:网络传输API工具类
 * Create by 罗旭东
 * Date 2014-7-30 下午4:39:47
 */
public class NetworkApiUtil {
	/** 访问来源  */
	public static final int COMEFROM = 1;
	
	/** 分页获取请求数据的最大数 */
	public static final int REQUEST_PAGE_NUM = 20;
	
	/** HTTP请求接口 */
	
	/** 请求参数属性名 */
	public static String HTTP_PARAMS = "httpParams";
	
	/** HTTP请求协议版本号 */
	public static final float HTTP_PROTOCOL_VERSION = 1.0f;
	
	/** Socket版本号 */
	public static final String SOCKET_PROTOCOL_VERSION = "V1.0";
	
	/** 注册接口地址 */
	public static final String URL_SERVICE_REG = "/remote/register/register";
	
	/** 登录接口地址 */
	public static final String URL_SERVICE_LOGIN = "/remote/login/login";
	
	private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	
	/**
	 * 
	 * @description:获取服务器地址
	 * @return String 服务器地址
	 */
	public static String getApiRootUrl(Context context) {
		if (MetaDataUtil.isRelease(context)) {
			return MetaDataUtil.getReleaseServer(context);
		} else {
			return MetaDataUtil.getDebugServer(context);
		}
	}
	
	/**
	 * 
	 * @description:获取完整url路径
	 * @param api 不带主域名的url
	 * @return String
	 */
	public static String getFullUrl(Context context, String api)
	{
		String rootUrl = getApiRootUrl(context);
		String fullUrl = null;
		
		if (rootUrl == null || rootUrl == "/")
		{
			rootUrl = "";
		}
		
		if (api == null || api == "/")
		{
			api = "";
		}
		
		fullUrl = rootUrl + api;
		
		if (rootUrl.length() != 0 && api.length() != 0)
		{
			if (rootUrl.lastIndexOf("/") != rootUrl.length() - 1 && api.indexOf("/") != 0)
			{
				fullUrl = rootUrl + "/" + api;
			}
			else if (rootUrl.lastIndexOf("/") == rootUrl.length() - 1 && api.indexOf("/") == 0 && api.length() > 1)
			{
				fullUrl = rootUrl + api.substring(1);
			}
			
		}
		
		return fullUrl;
	}
	
	/**
	 * 获取编码过的完整url路径
	 * @param context 上下文
	 * @param api 不带主域名的url
	 * @return String 处理后的url
	 */
	public static String getFullEncodeUrl(Context context, String api){
		String allowenUrlChar = "@#&=*+-_.,:!?()/~'%";
		return Uri.encode(getFullUrl(context, api), allowenUrlChar);
	}
	
	/**
	 * 获取完整url路径,自动填充url参数
	 * @param context 上下文
	 * @param api 不带主域名的url
	 * @param urlParams url参数
	 * @return String 处理后的url
	 */
	public static String getFullUrl(Context context, String api, Map<String, String> urlParams){
		String newUrl = getFullUrl(context, api);
		
		if (urlParams != null && urlParams.size() > 0){
			StringBuilder sb = new StringBuilder();
			
	        for(Entry<String, String> entry : urlParams.entrySet()) {
	        	if (TextUtils.isEmpty(entry.getValue())){
	        		continue;
	        	}
	        	
	        	if (sb.length() > 0){
	        		sb.append("&");
	        	}
	        	
	        	sb.append(entry.getKey() + "=" + entry.getValue());
	        }
	        
	        String queryParam = sb.toString();
	        
	        if (api.indexOf("?") < 0){
	        	newUrl += "?" + queryParam;
	        }else if (api.lastIndexOf("&") < (api.length() - 1)){
	        	newUrl += "&" + queryParam;
	        }else{
	        	newUrl += queryParam;
	        }
		}
		
		return newUrl;
	}
	
	/**
	 * 获取编码后完整url路径,自动填充url参数
	 * @param context 上下文
	 * @param api 不带主域名的url
	 * @param urlParams url参数
	 * @return String 处理后的url
	 */
	public static String getFullEncodeUrl(Context context, String api, Map<String, String> urlParams){
		String allowenUrlChar = "@#&=*+-_.,:!?()/~'%";
		return Uri.encode(getFullUrl(context, api, urlParams), allowenUrlChar);
	}
	
	/**
	 * 获取IP地址
	 * @param context
	 * @return
	 */
	public static String getServiceIp(Context context){
		String api = getApiRootUrl(context);
		if (TextUtils.isEmpty(api)){
			return null;
		}
		
		String ip = api.split(":")[0];
		
		if (IPV4_PATTERN.matcher(ip).matches() || IPV6_STD_PATTERN.matcher(ip).matches() || IPV6_HEX_COMPRESSED_PATTERN.matcher(ip).matches()){
			return ip;
		}
		
		return null;
	}
	
	/**
	 * 获取端口号
	 * @param context
	 * @return
	 */
	public static int getServicePort(Context context){
		String api = getApiRootUrl(context);
		if (TextUtils.isEmpty(api)){
			return 0;
		}
		
		String[] ips = api.split(":");
		
		if (ips == null || ips.length < 2){
			return 0;
		}
		
		try {
			return Integer.parseInt(ips[1]);
		} catch (Exception e) {
		}
		
		return 0;
	}
	
	/**
	 * 获取cookie
	 * @return
	 */
	public static Map<String, String> getCookieMap(){
		Map<String, String> headerParams = new ConcurrentHashMap<String, String>();
		if (!TextUtils.isEmpty(GlobalUtil.getSid())){
			//headerParams.put("Cookie", "JSESSIONID=" + GlobalUtil.getSid());
		}
		
		return headerParams;
	}
	
	/**
	 * 获取短信验证码cookie
	 * @return
	 */
	public static Map<String, String> getSmsCookie(){
		Map<String, String> headerParams = new ConcurrentHashMap<String, String>();
		if (!TextUtils.isEmpty(GlobalUtil.sSmsSessionId)){
			//headerParams.put("Cookie", "JSESSIONID=" + GlobalUtil.sSmsSessionId);
		}
		
		return headerParams;
	}
}

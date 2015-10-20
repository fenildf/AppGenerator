/**
 * Title: MetaDataUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-16 下午4:32:14
 * Version 1.0
 */
package com.gjfax.app.logic.utils;

import android.content.Context;
import android.text.TextUtils;

import com.luoxudong.app.utils.PackageUtil;

/** 
 * ClassName: MetaDataUtil
 * Description:读取meta数据工具类
 * Create by 罗旭东
 * Date 2013-8-16 下午4:32:14
 */
public class MetaDataUtil {
	/**
	 * 是否打印日志信息
	 * 
	 * @return
	 */
	public static boolean isShowLog(Context context) {
		return PackageUtil.getConfigBoolean(context, "LogEnable");
	}

	/**
	 * 是否显示未捕获异常信息
	 * 
	 * @return
	 */
	public static boolean isShowUncaughtexception(Context context) {
		return PackageUtil.getConfigBoolean(context, "ShowUncaughtexception");
	}

	/**
	 * 获得友盟APPkey
	 * 
	 * @return
	 */
	public static String getUmengAppKey(Context context) {
		return PackageUtil.getConfigString(context, "UMENG_APPKEY");
	}

	/**
	 * 获得友盟UMENG_CHANNEL
	 * 
	 * @return
	 */
	public static String getUmengChannel(Context context) {
		return PackageUtil.getConfigString(context, "UMENG_CHANNEL");
	}

	/**
	 * 获取后台可以解析的渠道号
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppChannel(Context context) {
		String appChannel = "0";

		String umengChannel = getUmengChannel(context);
		try {
			if (!TextUtils.isEmpty(umengChannel)) {
				String[] channel = umengChannel.split("_");

				if (channel != null && channel.length >= 1) {
					appChannel = channel[0];
				}
			}
		} catch (Exception e) {

		}
		return appChannel;
	}

	/**
	 * 获取百度位置Key
	 * 
	 * @param context
	 *            上下文
	 * @return String
	 */
	public static String getBaiduLocationKey(Context context) {
		return PackageUtil.getConfigString(context, "com.baidu.lbsapi.API_KEY");
	}

	/**
	 * 获得发布服务器地址
	 * 
	 * @return
	 */
	public static String getReleaseServer(Context context) {
		return PackageUtil.getConfigString(context, "ReleaseServer");
	}

	/**
	 * 获得发布服务器通信地址
	 * 
	 * @param context
	 * @return
	 * @return String
	 */
	public static String getAddressRelease(Context context) {
		return getReleaseServer(context);
	}

	/**
	 * 获得调试服务器通信地址
	 * 
	 * @param context
	 * @return
	 * @return String
	 */
	public static String getAddressDebug(Context context) {
		return getDebugServer(context);
	}

	/**
	 * 获得调试服务器地址
	 * 
	 * @return
	 */
	public static String getDebugServer(Context context) {
		return PackageUtil.getConfigString(context, "DebugServer");
	}

	/**
	 * 获得升级服务器地址
	 * 
	 * @return
	 */
	public static String getUpgradeServer(Context context) {
		return PackageUtil.getConfigString(context, "UpgradeServer");
	}

	/**
	 * 是否为发布版本
	 * 
	 * @return
	 */
	public static boolean isRelease(Context context) {
		return PackageUtil.getConfigBoolean(context, "IsRelease");
	}
}

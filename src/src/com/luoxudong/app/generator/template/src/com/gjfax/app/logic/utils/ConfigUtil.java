/**
 * Title: ConfigUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-7-30 下午2:44:08
 * Version 1.0
 */
package com.gjfax.app.logic.utils;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;

import com.gjfax.app.logic.common.BaseConstant;
import com.gjfax.app.logic.network.http.model.vo.QueryWXShareInfoRet;
import com.google.gson.Gson;
import com.luoxudong.app.utils.FileUtil;

/**
 * ClassName: ConfigUtil Description:配置信息操作工具类 Create by 罗旭东 Date 2014-7-30
 * 下午2:44:08
 */
public class ConfigUtil {
	/** 系统基础配置文件 */
	private static final String SYS_CONFIG_FILE_NAME = BaseConstant.SYSTEM_CONFIG_FILE_NAME;

	/** 用户基本配置信息 */
	private static final String USER_CONFIG_FILE_NAME = BaseConstant.USER_CONFIG_FILE_NAME;

	/** 主目录路径 */
	private static String sAppRootDirPath = null;

	/** 当前用户主目录 */
	private static String sCurrentUserHomePath = null;

	/** 用户配置信息 */
	private static String sCurrentUserConfigPath = null;

	/** 同步锁 */
	private static byte[] sSynByte = new byte[1];

	/** 系统配置信息 */
	private static SystemConfigInfo systemConfigInfo = null;

	/** 用户配置信息 */
	private static UserConfigInfo userConfigInfo = null;

	/**
	 * 初始化系统目录
	 * 
	 * @param context
	 *            上下文
	 */
	public static void initSysDirs(Context context) {
		sAppRootDirPath = context.getDir(BaseConstant.ROOT_DIR_NAME,
				Context.MODE_PRIVATE).getAbsolutePath();
	}

	/**
	 * 初始化用户目录
	 * 
	 * @param context
	 * @param currentUserId
	 */
	public static void initUserDir(Context context, String currentUserId) {
		if (TextUtils.isEmpty(currentUserId)) {
			currentUserId = BaseConstant.DEFAULT_USER;
		}

		sCurrentUserHomePath = sAppRootDirPath + File.separator + currentUserId;
		sCurrentUserConfigPath = sCurrentUserHomePath + File.separator
				+ "config";
	}

	/**
	 * 获取用户登录ID
	 * 
	 * @return
	 */
	public static String getUserId() {
		checkSystemConfig();
		return systemConfigInfo.getUserId();
	}

	/**
	 * 保存用户登录ID
	 * 
	 * @param userId
	 */

	public static void setUserId(String userId) {
		checkSystemConfig();
		systemConfigInfo.setUserId(userId);
		writeSystemConfig();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public static String getPassword() {
		checkSystemConfig();
		return systemConfigInfo.getPassword();
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 */
	public static void setPassword(String password) {
		checkSystemConfig();
		systemConfigInfo.setPassword(password);
		writeSystemConfig();
	}

	/**
	 * 是否开启自动登录
	 * 
	 * @return
	 */
	public static boolean isAutoLogin() {
		checkSystemConfig();
		return systemConfigInfo.isAutoLogin();
	}

	/**
	 * 设置自动登录开关
	 * 
	 * @param autoLogin
	 */
	public static void setAutoLogin(boolean autoLogin) {
		checkSystemConfig();
		systemConfigInfo.setAutoLogin(autoLogin);
		writeSystemConfig();
	}

	/**
	 * 获取本地数据库最新版本号
	 * 
	 * @return int 数据库版本号
	 */
	public static int getLatestDbVersion() {
		checkUserConfig();
		return userConfigInfo.getLatestDbVersion();
	}

	/**
	 * 设置本地数据库最新版本号
	 * 
	 * @param latestDbVersion
	 *            最新版本号
	 */
	public static void setLatestDbVersion(int latestDbVersion) {
		checkUserConfig();
		userConfigInfo.setLatestDbVersion(latestDbVersion);
		writeUserConfig();
	}

	/**
	 * 是否显示启动页新功能引导
	 * 
	 * @return
	 */
	public static boolean isShowGuide() {
		checkSystemConfig();
		return systemConfigInfo.isShowGuide();
	}

	/**
	 * 设置是否需要显示启动页新功能引导
	 * 
	 * @param showGuide
	 */
	public static void setShowGuide(boolean showGuide) {
		checkSystemConfig();
		systemConfigInfo.setShowGuide(showGuide);
		writeSystemConfig();
	}

	/**
	 * 是否记住用户名
	 * @return
	 */
	public static boolean isRememberUserId() {
		checkSystemConfig();
		return systemConfigInfo.isRememberUserId();
	}

	/**
	 * 设置是否记住用户名
	 * @param rememberUserId
	 */
	public static void setRememberUserId(boolean rememberUserId) {
		checkSystemConfig();
		systemConfigInfo.setRememberUserId(rememberUserId);
		writeSystemConfig();
	}
	
	/**
	 * 获取启动页图片路径
	 * 
	 * @return
	 */
	public static String getSplashImgPath() {
		checkSystemConfig();
		return systemConfigInfo.getSplashImgPath();
	}

	/**
	 * 设置启动页图片路径
	 * 
	 * @param splashImgPath
	 */
	public static void setSplashImgPath(String splashImgPath) {
		checkSystemConfig();
		systemConfigInfo.setSplashImgPath(splashImgPath);
		writeSystemConfig();
	}

	/**
	 * 获取服务器最新版本号
	 * 
	 * @return
	 */
	public static int getServerVersionNo() {
		checkSystemConfig();
		return systemConfigInfo.getServerVersionNo();
	}

	/**
	 * 设置服务器最新版本号
	 * 
	 * @param serverVersionNo
	 */
	public static void setServerVersionNo(int serverVersionNo) {
		checkSystemConfig();
		systemConfigInfo.setServerVersionNo(serverVersionNo);
		writeSystemConfig();
	}
	
	/**
	 * 检测系统配置
	 * 
	 */
	private static void checkSystemConfig() {
		if (systemConfigInfo == null) {
			readSystemConfig();
			if (systemConfigInfo == null) {
				systemConfigInfo = new ConfigUtil().new SystemConfigInfo();
			}

		}
	}

	/**
	 * 检测用户配置
	 */
	private static void checkUserConfig() {
		if (userConfigInfo == null) {
			readUserConfig();

			if (userConfigInfo == null) {
				userConfigInfo = new ConfigUtil().new UserConfigInfo();
			}

		}
	}

	/**
	 * 读取系统配置信息
	 * 
	 */
	private static void readSystemConfig() {
		File file = new File(sAppRootDirPath, SYS_CONFIG_FILE_NAME);
		if (!file.exists()) {
			systemConfigInfo = null;
			return;
		}
		String systemConfigJson = FileUtil.readFileFromSDCard(sAppRootDirPath,
				SYS_CONFIG_FILE_NAME);

		Gson gson = new Gson();
		systemConfigInfo = gson.fromJson(systemConfigJson,
				SystemConfigInfo.class);
	}

	/**
	 * 读取用户配置信息
	 * 
	 * @return void
	 */
	private static void readUserConfig() {
		File file = new File(sCurrentUserConfigPath, USER_CONFIG_FILE_NAME);
		if (!file.exists()) {
			userConfigInfo = null;
			return;
		}

		String userConfigJson = FileUtil.readFileFromSDCard(
				sCurrentUserConfigPath, USER_CONFIG_FILE_NAME);

		Gson gson = new Gson();
		userConfigInfo = gson.fromJson(userConfigJson, UserConfigInfo.class);
	}

	private static void writeSystemConfig() {
		File file = new File(sAppRootDirPath);

		if (!file.exists()) {
			file.mkdirs();
		}

		checkSystemConfig();

		Gson gson = new Gson();
		String systemConfigJson = gson.toJson(systemConfigInfo);
		synchronized (sSynByte) {
			try {
				FileUtil.saveFileToSDCard(sAppRootDirPath,
						SYS_CONFIG_FILE_NAME, systemConfigJson);
			} catch (Exception e) {
			}
		}

	}

	private static void writeUserConfig() {
		File file = new File(sCurrentUserConfigPath);

		if (!file.exists()) {
			file.mkdirs();
		}

		checkUserConfig();

		Gson gson = new Gson();
		String userConfigJson = gson.toJson(userConfigInfo);
		synchronized (sSynByte) {
			try {
				FileUtil.saveFileToSDCard(sCurrentUserConfigPath,
						USER_CONFIG_FILE_NAME, userConfigJson);
			} catch (Exception e) {
			}
		}
	}

	public static String getsAppRootDirPath() {
		return sAppRootDirPath;
	}

	public class SystemConfigInfo {
		/** 用户ID */
		private String mUserId = null;

		/** 登录密码 */
		private String mPassword = null;

		/** 是否记住用户名 */
		private boolean mRememberUserId = false;

		/** 是否自动登录 */
		private boolean mAutoLogin = false;

		/** 是否需要显示帮助向导 */
		private boolean mNeedShowGuide = true;

		/** 每次进入app从服务器获取的最新版本号*/
		public int mServerVersionNo = 0;

		/** 显示新功能引导 */
		private boolean mShowGuide = true;

		/** 启动页图片地址 */
		private String mSplashImgPath = null;

		public String getUserId() {
			return mUserId;
		}

		public void setUserId(String userId) {
			mUserId = userId;
		}
		
		public String getPassword() {
			return mPassword;
		}

		public void setPassword(String password) {
			mPassword = password;
		}
		
		public int getServerVersionNo() {
			return mServerVersionNo;
		}

		public void setServerVersionNo(int serverVersionNo) {
			mServerVersionNo = serverVersionNo;
		}

		public boolean isAutoLogin() {
			return mAutoLogin;
		}

		public void setAutoLogin(boolean autoLogin) {
			mAutoLogin = autoLogin;
		}

		public boolean isNeedShowGuide() {
			return mNeedShowGuide;
		}

		public void setNeedShowGuide(boolean needShowGuide) {
			mNeedShowGuide = needShowGuide;
		}

		public String getSplashImgPath() {
			return mSplashImgPath;
		}

		public void setSplashImgPath(String splashImgPath) {
			mSplashImgPath = splashImgPath;
		}

		public boolean isShowGuide() {
			return mShowGuide;
		}

		public void setShowGuide(boolean showGuide) {
			mShowGuide = showGuide;
		}

		public boolean isRememberUserId() {
			return mRememberUserId;
		}

		public void setRememberUserId(boolean rememberUserId) {
			mRememberUserId = rememberUserId;
		}

	}

	public class UserConfigInfo {
		/** 数据库最新版本号 */
		private int mLatestDbVersion = -1;
		
		public int getLatestDbVersion() {
			return mLatestDbVersion;
		}

		public void setLatestDbVersion(int latestDbVersion) {
			mLatestDbVersion = latestDbVersion;
		}
	}

}

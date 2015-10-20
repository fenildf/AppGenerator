package com.gjfax.app.logic.upgrade.interfaces;

import android.content.Context;

import com.gjfax.app.logic.upgrade.manager.UpgradeCallable;


/**
 * Title: IUpgradeManager.java
 * Description: 
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Author: 罗旭东
 * Date: 2015-3-23 下午1:38:16
 * Version: 1.0
 */
public interface IUpgradeManager {
	/**
	 * 检测apk最新版本
	 * @param context
	 * @param callable
	 */
	public void upgradeCheck(Context context, UpgradeCallable callable);
	
	
	/**
	 * 初始化升级后的数据
	 * @param context
	 */
	public void initUpgradeData(Context context);
	
}

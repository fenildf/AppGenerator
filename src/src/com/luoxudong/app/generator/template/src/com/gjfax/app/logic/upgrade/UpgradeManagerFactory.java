/**
 * Title: UserFactory.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author:何建明
 * Date: 2015-3-23 上午9:51:37
 * Version: 1.0
 */
package com.gjfax.app.logic.upgrade;

import com.gjfax.app.logic.upgrade.interfaces.IUpgradeManager;
import com.gjfax.app.logic.upgrade.manager.UpgradeManager;
import com.luoxudong.app.singletonfactory.SingletonFactory;

/** 
 * ClassName: UpgradeManagerFactory
 * Description: TODO(这里用一句话描述这个类的作用)
 * Create by: 何建明
 * Date: 2015-3-23 上午9:51:37
 */
public class UpgradeManagerFactory {
	public static IUpgradeManager getUpgradeManager(){
		return SingletonFactory.getInstance(UpgradeManager.class);
	}
}

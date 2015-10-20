/**
 * Title: UserFactory.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2014-9-15 上午9:51:37
 * Version: 1.0
 */
package com.gjfax.app.logic.user;

import com.gjfax.app.logic.user.interfaces.IUserManager;
import com.gjfax.app.logic.user.manager.UserManager;
import com.luoxudong.app.singletonfactory.SingletonFactory;

/** 
 * ClassName: UserFactory
 * Description: TODO(这里用一句话描述这个类的作用)
 * Create by: 罗旭东
 * Date: 2014-9-15 上午9:51:37
 */
public class UserFactory {
	public static IUserManager getUserManager(){
		return SingletonFactory.getInstance(UserManager.class);
	}
}

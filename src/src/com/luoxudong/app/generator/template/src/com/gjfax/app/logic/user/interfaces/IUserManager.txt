/**
 * Title: IUserManager.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2014-9-15 上午9:51:52
 * Version: 1.0
 */
package #{AppPackage}.logic.user.interfaces;

import android.content.Context;

import #{AppPackage}.logic.user.manager.UserCallable;

/** 
 * ClassName: IUserManager
 * Description: 用户管理类接口
 * Create by: 罗旭东
 * Date: 2014-9-15 上午9:51:52
 */
public interface IUserManager {
	/**
	 * 发送登录请求
	 * @param context 上下文
	 * @param userId 登录名
	 * @param password 登录密码
	 * @param callable 登录回调
	 */
	public void loginRequest(Context context, String userId, String password, UserCallable callable);
}

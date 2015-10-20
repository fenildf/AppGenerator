/**
 * Title: GlobalUtil.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-8-6 下午2:21:43
 * Version 1.0
 */
package com.gjfax.app.logic.utils;

import com.gjfax.app.logic.db.model.UserInfo;


/** 
 * ClassName: GlobalUtil
 * Description:全局变量工具类
 * Create by 罗旭东
 * Date 2014-8-6 下午2:21:43
 */
public class GlobalUtil {
	/** 用户信息 */
	public static UserInfo sUserInfo = null;
	
	public static String getSid() {
		return sUserInfo == null ? null : sUserInfo.getSid();
	}

	public static void setSid(String sid) {		
		if (sUserInfo != null){
			sUserInfo.setSid(sid);
		}
	}
}

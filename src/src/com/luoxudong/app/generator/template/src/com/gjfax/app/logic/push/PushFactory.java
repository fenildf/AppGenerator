/**
 * Title: PushFactory.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-8-27 下午3:32:42
 * Version 1.0
 */
package com.gjfax.app.logic.push;

import com.gjfax.app.logic.push.interfaces.IPushManager;
import com.gjfax.app.logic.push.manager.PushManager;
import com.luoxudong.app.singletonfactory.SingletonFactory;

/** 
 * ClassName: PushFactory
 * Description:推送消息工厂类
 * Create by 罗旭东
 * Date 2014-8-27 下午3:32:42
 */
public class PushFactory {
	public static IPushManager getPushManager(){
		return SingletonFactory.getInstance(PushManager.class);
	}
}

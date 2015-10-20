/**
 * Title: IPushManager.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-8-27 下午3:33:00
 * Version 1.0
 */
package com.gjfax.app.logic.push.interfaces;

import android.content.Context;

/** 
 * ClassName: IPushManager
 * Description:推送管理接口
 * Create by 罗旭东
 * Date 2014-8-27 下午3:33:00
 */
public interface IPushManager {
	/**
	 * 解析推送消息
	 * @param context 上下文
	 * @param message 消息内容
	 */
	public void filterPusMessage(Context context, String message);
}

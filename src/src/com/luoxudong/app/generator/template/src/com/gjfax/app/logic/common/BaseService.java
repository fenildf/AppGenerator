/**
 * Title: BaseService.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-1-9 下午10:02:42
 * Version 1.0
 */
package com.gjfax.app.logic.common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.luoxudong.app.utils.LogUtil;

/** 
 * ClassName: BaseService
 * Description:app服务
 * Create by 罗旭东
 * Date 2014-1-9 下午10:02:42
 */
public class BaseService extends Service{
	private static final String TAG = BaseService.class.getSimpleName();
	
	/** 服务是否初始化*/
	public static boolean sIsInitService = false;
	
	@Override
	public void onCreate() {
		LogUtil.i(TAG, "正在启动服务...");
		super.onCreate();
		
		sIsInitService = true;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}

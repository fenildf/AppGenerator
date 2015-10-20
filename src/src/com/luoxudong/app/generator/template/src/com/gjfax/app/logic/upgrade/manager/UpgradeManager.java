/**
 * Title: UpgradeManager.java
 * Description: 
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company: 深圳市彩讯科技有限公司
 * Author: 罗旭东
 * Date: 2015年4月8日 上午9:31:02
 * Version: 1.0
 */
package com.gjfax.app.logic.upgrade.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.gjfax.app.logic.network.NetworkApiUtil;
import com.gjfax.app.logic.upgrade.interfaces.IUpgradeManager;
import com.gjfax.app.logic.utils.ConfigUtil;
import com.luoxudong.app.asynchttp.AsyncHttpUtil;
import com.luoxudong.app.asynchttp.callable.SimpleRequestCallable;
import com.luoxudong.app.utils.PackageUtil;

/** 
 * ClassName: UpgradeManager
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by: 
 * Date: 2015年4月8日 上午9:31:02
 */
public class UpgradeManager implements IUpgradeManager {

	@Override
	public void upgradeCheck(final Context context, final UpgradeCallable callable) {
		AsyncHttpUtil.simpleGetHttpRequest(NetworkApiUtil.getFullUrl(context, NetworkApiUtil.URL_SERVICE_UPDATE), new SimpleRequestCallable() {
			
			@Override
			public void onFailed(int errorCode, String errorMsg) {
				if (callable != null){
					callable.checkErrorCode(context, errorCode, errorMsg);
				}
			}
			
			@Override
			public void onSuccess(String responseInfo) {
				if (!TextUtils.isEmpty(responseInfo)){
					String url = null;//apk下载链接
					String versionName = null;//版本号名称
					int versionNo = 0;//版本号，用于判断是否与当前版相同
					String appName = null;//app名称，用于下载保存文件
					String desc = null;//升级功能描述
					boolean isForce = false;//是否强制升级
					
					InputStream is = new ByteArrayInputStream(responseInfo.getBytes());  
					
					XmlPullParser xmlpull = Xml.newPullParser();//xml解析器
					try {
						xmlpull.setInput(is, "utf-8");
						int eventCode = xmlpull.getEventType();
						
						//一直循环知道文档遍历结束
						while (eventCode != XmlPullParser.END_DOCUMENT) {
							switch (eventCode) {
							case XmlPullParser.START_DOCUMENT:// 开始文档 new数组
								break;
							case XmlPullParser.START_TAG:
								String name = xmlpull.getName();
								if (("url".equals(name))) {
									url = xmlpull.nextText();
								} else if ("versionName".equals(name)) {
									versionName = xmlpull.nextText();
								} else if ("versionNo".equals(name)) {
									versionNo = Integer.parseInt(xmlpull.nextText());
								} else if ("appName".equals(name)) {
									appName = xmlpull.nextText();
								} else if ("desc".equals(name)){
									desc = xmlpull.nextText();
								} else if ("is_force".equals(name)){
									try {
										isForce = Integer.parseInt(xmlpull.nextText()) == 1 ? true : false;
									} catch (Exception e) {
										
									}
								}
								break;
							case XmlPullParser.END_TAG:
								break;
							}
							
							eventCode = xmlpull.next();// 没有结束xml文件就推到下个进行解析
						}
						//versionNo = 1000;
						//isForce = true;
						ConfigUtil.setServerVersionNo(versionNo);
						if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(appName) && versionNo > PackageUtil.getVersionCode(context)){//根据版本号判断是否有升级包							
							if (callable != null){
								callable.onFindNewVersion(url, versionName, appName, desc, null, 0, isForce);
							}
							return;
						}
					} catch (XmlPullParserException e1) {
					} catch (IOException e) {
					}
				}
				
				if (callable != null){
					callable.onUnFindNewVersion();
				}
			}
		});
	}

	@Override
	public void initUpgradeData(Context context) {
		
	}

}

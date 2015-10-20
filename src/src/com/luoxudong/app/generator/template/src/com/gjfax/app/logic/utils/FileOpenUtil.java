/**
 * Title: FileOpenUtil.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年8月30日 下午5:47:50
 * Version: 1.0
 */
package com.gjfax.app.logic.utils;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.gjfax.app.ui.utils.ToastUtil;

/** 
 * <pre>
 * ClassName: FileOpenUtil
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by: 罗旭东
 * Date: 2015年8月30日 下午5:47:50
 * </pre>
 */
public class FileOpenUtil {
	public static void openPdfFile(Context context, String filePath){
		Uri path = Uri.fromFile(new File(filePath));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(path, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			ToastUtil.showToast(context, "不支持打开PDF格式，请先下载PDF打开插件!");
		}
	}
}

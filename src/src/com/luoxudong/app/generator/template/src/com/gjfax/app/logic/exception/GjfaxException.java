/**
 * Title: GjfaxException.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-2 下午4:00:44
 * Version 1.0 
 */
package com.gjfax.app.logic.exception; 	

import com.luoxudong.app.utils.LogUtil;

/** 
 * ClassName: GjfaxException 
 * Description:自定义异常处理类
 * Create by 罗旭东
 * Date 2013-8-2 下午4:00:44 
 */
public class GjfaxException extends RuntimeException {
	private static final String TAG = GjfaxException.class.getName();
	
	private static final long serialVersionUID = 1L;
	
	private int mExceptionCode = -1;
	
	public GjfaxException(String message) {
		super(message);
		LogUtil.e(TAG, message);
	}
	
	public GjfaxException(String message, Throwable throwable)
	{
		super(message, throwable);
		LogUtil.e(TAG, message);
	}
	
	public GjfaxException(int exceptionCode, Throwable throwable)
	{
		super(throwable.toString(), throwable);
		setExceptionCode(exceptionCode);
		LogUtil.e(TAG, exceptionCode + "");
		
	}
	
	public GjfaxException(int exceptionCode, String message)
	{
		super(message);
		setExceptionCode(exceptionCode);
		LogUtil.e(TAG, exceptionCode + "");
		
	}

	public int getExceptionCode() {
		return mExceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		mExceptionCode = exceptionCode;
	}
	
	
}

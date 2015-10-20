/**
 * Title: ServerResult.java
 * Description:
 * Copyright: Copyright (c) 2008
 * Company:www.bchun.com
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-11-20 下午2:16:47
 * Version 1.0
 */
package com.bchun.jobs.logic.network.http.model.vo;

import java.io.Serializable;

/** 
 * ClassName: ServerResult
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by 罗旭东
 * Date 2013-11-20 下午2:16:47
 */
public class ServerResult implements Serializable{
	private static final long serialVersionUID = 1L;

	private int resultCode = -1;
	private String resultMessage = null;
	private String responseTime = null;
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
}

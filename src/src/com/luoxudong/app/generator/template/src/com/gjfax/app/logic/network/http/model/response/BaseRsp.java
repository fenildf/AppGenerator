/**
 * Title: BaseResponse.java
 * Description:
 * Copyright: Copyright (c) 2008
 * Company:深圳彩讯科技有限公司
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-9 下午5:06:45
 * Version 1.0 
 */
package com.bchun.jobs.logic.network.http.model.response;

import com.bchun.jobs.logic.network.http.model.vo.ServerResult;
import com.luoxudong.app.asynchttp.model.BaseResponse;

/** 
 * ClassName: BaseResponse 
 * Description:json请求返回参数公共属性
 * Create by 罗旭东
 * Date 2013-8-9 下午5:06:45 
 */
public class BaseRsp<M> extends BaseResponse<M>{
	private static final long serialVersionUID = 1L;
	
	private ServerResult serverResult = null;
	
	public ServerResult getServerResult() {
		return serverResult;
	}
	
	public void setServerResult(ServerResult serverResult) {
		this.serverResult = serverResult;
	}
}

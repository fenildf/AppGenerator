/**
 * Title: JobsJsonHttpResponseAdapter.java
 * Description:
 * Copyright: Copyright (c) 2013 richinfo.cn
 * Company:深圳彩讯科技有限公司
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-8-12 下午3:48:50
 * Version 1.0
 */
package com.bchun.jobs.logic.network.http;

import android.content.Intent;
import android.text.TextUtils;

import com.bchun.jobs.logic.common.BaseApplication;
import com.bchun.jobs.logic.exception.JobsExceptionCode;
import com.bchun.jobs.logic.network.http.model.response.BaseRsp;
import com.bchun.jobs.logic.user.UserFactory;
import com.bchun.jobs.logic.user.manager.UserCallable;
import com.bchun.jobs.logic.utils.ConfigUtil;
import com.bchun.jobs.ui.activities.LoginActivity;
import com.luoxudong.app.asynchttp.adapter.BaseJsonHttpResponseAdapter;
import com.luoxudong.app.asynchttp.model.BaseResponse;

/** 
 * ClassName: JobsJsonHttpResponseAdapter
 * Description:http请求返回参数解析,过滤失败信息
 * Create by 罗旭东
 * Date 2014-8-12 下午3:48:50
 */
public class JobsJsonHttpResponseAdapter extends BaseJsonHttpResponseAdapter {
	
	@Override
	public synchronized <M extends BaseResponse<M>> boolean checkResponseData(M response) {
		if (response == null){
			errorCode = JobsExceptionCode.unknown.getErrorCode();
			errorMsg = "返回结果不能为空!";
			return false;
		}
		
		if (response instanceof BaseRsp){
			BaseRsp<M> rsp = (BaseRsp<M>)response;
			
			if (rsp.getServerResult() != null && rsp.getServerResult().getResultCode() == 200){
				return true;
			}else if (rsp.getServerResult() != null && rsp.getServerResult().getResultCode() == 205){
				errorCode = JobsExceptionCode.passwordError.getErrorCode();
				errorMsg = rsp.getServerResult().getResultMessage();
			}else if (rsp.getServerResult() != null && rsp.getServerResult().getResultCode() == 204){//sid失效
				errorCode = JobsExceptionCode.sessionTimeout.getErrorCode();
				errorMsg = "登陆超时，请重试!";
				if (!TextUtils.isEmpty(ConfigUtil.getUserId()) && !TextUtils.isEmpty(ConfigUtil.getPassword())){//自动登录
					UserFactory.getUserManager().loginRequest(BaseApplication.getInstance(), ConfigUtil.getUserId(), ConfigUtil.getPassword(), ConfigUtil.getUserType(), new UserCallable() {
						
						@Override
						public void onCallbackFail(int errorCode, String msg) {
							BaseApplication.getInstance().startActivity(new Intent(BaseApplication.getInstance(), LoginActivity.class));
						}
					});
				}else{
					BaseApplication.getInstance().startActivity(new Intent(BaseApplication.getInstance(), LoginActivity.class));
				}
				
			}else{
				errorCode = JobsExceptionCode.unknown.getErrorCode();
				errorMsg = rsp.getServerResult().getResultMessage();//CharsetUtil.decodeUnicode(rsp.getServerResult().getResultMessage());
			}
			
		}else{
			errorCode = JobsExceptionCode.unknown.getErrorCode();
			errorMsg = "返回数据类型错误!";
		}
		
		return false;
	}
}

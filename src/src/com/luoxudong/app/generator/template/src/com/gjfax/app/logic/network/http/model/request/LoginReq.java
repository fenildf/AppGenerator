package com.bchun.jobs.logic.network.http.model.request;

public class LoginReq extends BaseReq<LoginReq>{
	private static final long serialVersionUID = 1L;
	
	/** 登录手机号码 */
	private String userName = null;
	
	/** 登录 密码 */
	private String password = null;
	
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

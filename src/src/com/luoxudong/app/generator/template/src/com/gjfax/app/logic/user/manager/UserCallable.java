/**
 * Title: UserCallable.java
 * Description: 
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company: 个人
 * Author: 罗旭东
 * Date: 2015年5月24日 下午10:47:08
 * Version: 1.0
 */
package com.gjfax.app.logic.user.manager;

import java.util.List;

import com.gjfax.app.logic.common.BaseCallable;
import com.gjfax.app.logic.db.model.UserInfo;
import com.gjfax.app.logic.network.http.model.vo.FundAccountRet;
import com.gjfax.app.logic.network.http.model.vo.GetUserInfoRet;
import com.gjfax.app.logic.network.http.model.vo.RecommendItem;
import com.gjfax.app.logic.network.http.model.vo.RegRet;

/** 
 * ClassName: UserCallable
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by: 
 * Date: 2015年5月24日 下午10:47:08
 */
public abstract class UserCallable extends BaseCallable {
	/**
	 * 登录成功
	 * @param userInfo
	 */
	public void login(UserInfo userInfo){};
}

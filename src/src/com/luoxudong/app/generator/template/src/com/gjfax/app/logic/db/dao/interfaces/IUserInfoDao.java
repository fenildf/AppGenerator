/**
 * Title: IUserInfoDao.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-5-27 下午1:52:21
 * Version 1.0
 */
package com.gjfax.app.logic.db.dao.interfaces;

import com.gjfax.app.logic.db.model.UserInfo;
import com.luoxudong.app.database.interfaces.IBaseDao;

/**
 * ClassName: IUserInfoDao Description:TODO(这里用一句话描述这个类的作用) Create by 罗旭东 Date
 * 2014-5-27 下午1:52:21
 */
public interface IUserInfoDao extends IBaseDao<UserInfo, Long> {
	/**
	 * 根据登录账号获取用户信息
	 * 
	 * @param userId
	 *            登录账号
	 */
	public UserInfo findUserInfoByUserId(String userId);

}

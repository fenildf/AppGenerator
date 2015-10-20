/**
 * Title: UserManager.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2014-9-15 上午9:52:16
 * Version: 1.0
 */
package com.gjfax.app.logic.user.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.gjfax.app.logic.account.MyAccoutFactory;
import com.gjfax.app.logic.account.manager.MyAccountCallable;
import com.gjfax.app.logic.common.BaseApplication;
import com.gjfax.app.logic.common.BaseConstant;
import com.gjfax.app.logic.db.dao.UserInfoDao;
import com.gjfax.app.logic.db.dao.interfaces.IUserInfoDao;
import com.gjfax.app.logic.db.model.UserInfo;
import com.gjfax.app.logic.exception.GjfaxExceptionCode;
import com.gjfax.app.logic.network.NetworkApiUtil;
import com.gjfax.app.logic.network.http.model.request.DefaultReq;
import com.gjfax.app.logic.network.http.model.request.GetUserFundAccountReq;
import com.gjfax.app.logic.network.http.model.request.GetUserInfoReq;
import com.gjfax.app.logic.network.http.model.request.LoginReq;
import com.gjfax.app.logic.network.http.model.request.RegReq;
import com.gjfax.app.logic.network.http.model.request.UpdateNickNameReq;
import com.gjfax.app.logic.network.http.model.request.VerifyInvitationCodeReq;
import com.gjfax.app.logic.network.http.model.request.VerifyLoginPwdReq;
import com.gjfax.app.logic.network.http.model.request.VerifyUserNameReq;
import com.gjfax.app.logic.network.http.model.response.DefaultRsp;
import com.gjfax.app.logic.network.http.model.response.GetRecommendListRsp;
import com.gjfax.app.logic.network.http.model.response.GetUserFundAccountRsp;
import com.gjfax.app.logic.network.http.model.response.GetUserInfoRsp;
import com.gjfax.app.logic.network.http.model.response.LoginRsp;
import com.gjfax.app.logic.network.http.model.response.RegRsp;
import com.gjfax.app.logic.network.http.model.response.VerifyInvitationCodeRsp;
import com.gjfax.app.logic.network.http.model.vo.GetMyAccountInfoRet;
import com.gjfax.app.logic.network.http.model.vo.GetUserInfoRet;
import com.gjfax.app.logic.rewards.RewardsFactory;
import com.gjfax.app.logic.user.interfaces.IUserManager;
import com.gjfax.app.logic.utils.ConfigUtil;
import com.gjfax.app.logic.utils.GlobalUtil;
import com.gjfax.app.logic.utils.MetaDataUtil;
import com.luoxudong.app.asynchttp.AsyncHttpUtil;
import com.luoxudong.app.asynchttp.callable.JsonRequestCallable;
import com.luoxudong.app.database.DaoManagerFactory;
import com.luoxudong.app.singletonfactory.SingletonFactory;

/**
 * ClassName: UserManager Description: TODO(这里用一句话描述这个类的作用) Create by: 罗旭东 Date:
 * 2014-9-15 上午9:52:16
 */
public class UserManager implements IUserManager {
	private IUserInfoDao mUserInfoDao = null;

	@SuppressWarnings("unused")
	private void checkUserInfoDao() {
		if (mUserInfoDao == null) {
			mUserInfoDao = DaoManagerFactory.getDaoManager(BaseConstant.DATABASE_PATH).getDataHelper(UserInfoDao.class, UserInfo.class);
		}
	}

	@Override
	public void loginRequest(final Context context, String userId, String password, final UserCallable callable) {
		LoginReq req = new LoginReq();
		req.setUserName(userId);
		req.setPassword(password);
		AsyncHttpUtil.formPostHttpRequest(NetworkApiUtil.getFullUrl(context, NetworkApiUtil.URL_SERVICE_LOGIN), NetworkApiUtil.HTTP_PARAMS, req, LoginRsp.class, new JsonRequestCallable<LoginRsp>() {

			@Override
			public void onFailed(int errorCode, String errorMsg) {
				if (callable != null){
					callable.checkErrorCode(context, errorCode, errorMsg);
				}
			}
			
			@Override
			public void onSuccess(LoginRsp responseInfo) {
				UserInfo userInfo = new UserInfo();
				userInfo.setSid(responseInfo.getSessionId());
				
				if (responseInfo.getResult() != null){
					if (!TextUtils.isEmpty(responseInfo.getResult().getActivation())){
						userInfo.setActivation(Integer.parseInt(responseInfo.getResult().getActivation()));
					}
					userInfo.setBindCard("true".equals(responseInfo.getResult().getIsBindCard()) ? 1 : 0);
					userInfo.setCertiCode(responseInfo.getResult().getCertiCode());
					userInfo.setCertiCodeAll(responseInfo.getResult().getCertiCodeAll());
					userInfo.setEmail(responseInfo.getResult().getEmail());
					userInfo.setEquityAccount(responseInfo.getResult().getFidGqzh());
					userInfo.setHeadUrl(responseInfo.getResult().getHeadPortraitPath());
					userInfo.setMobile(responseInfo.getResult().getMobilePhone());
					userInfo.setNickName(responseInfo.getResult().getNickName());
					if (!TextUtils.isEmpty(responseInfo.getResult().getNickName())) {
						ConfigUtil.setUserNameForGesture(responseInfo.getResult().getNickName());
					}else {
						ConfigUtil.setUserNameForGesture(responseInfo.getResult().getUserName());
					}
					userInfo.setRealName(responseInfo.getResult().getRealName());
					userInfo.setRecommendCode(responseInfo.getResult().getRecommendCode());
					userInfo.setSetCert("true".equals(responseInfo.getResult().getIsSetCert()) ? 1 : 0);
					userInfo.setSetDealpwd("true".equals(responseInfo.getResult().getIsSetDealpwd()) ? 1 : 0);
					userInfo.setSetPwdAnswer("true".equals(responseInfo.getResult().getIsSetPwdAnswer()) ? 1 : 0);
					userInfo.setUserId(responseInfo.getResult().getUserId());
					userInfo.setUserName(responseInfo.getResult().getUserName());
					userInfo.setValidateEmail("true".equals(responseInfo.getResult().getEmail()) ? 1 : 0);
				}
				
				boolean isAnother = false;
				if (GlobalUtil.sUserInfo != null && !GlobalUtil.sUserInfo.getUserName().equals(userInfo.getUserName())){//切换用户
					isAnother = true;
					ConfigUtil.setNeedGesturePwd(false);
				}
				
				GlobalUtil.sUserInfo = userInfo;
				
				//更新未读奖励状态
				RewardsFactory.getRewardsManager().checkUnknowAward(BaseApplication.getInstance(), null);
				if (callable != null){
					if (isAnother){
						callable.loginAnother(userInfo);
					}else{
						callable.login(userInfo);
					}
				}
			}
		});
	}
	
}

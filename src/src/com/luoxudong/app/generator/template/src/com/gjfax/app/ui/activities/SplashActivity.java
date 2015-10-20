/**
 * Title: SplashActivity.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-06 上午10:10:44
 * Version 1.0
 */
package com.gjfax.app.ui.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.gjfax.app.R;
import com.gjfax.app.logic.common.BaseConstant;
import com.gjfax.app.logic.invest.InvestFactory;
import com.gjfax.app.logic.invest.manager.InvestCallable;
import com.gjfax.app.logic.network.http.model.vo.GetNewsResult;
import com.gjfax.app.logic.network.http.model.vo.NewUserProduct;
import com.gjfax.app.logic.network.http.model.vo.QueryWXShareInfoRet;
import com.gjfax.app.logic.network.http.model.vo.RecommendProduct;
import com.gjfax.app.logic.news.NewsFactory;
import com.gjfax.app.logic.news.manager.NewsCallable;
import com.gjfax.app.logic.utils.ConfigUtil;
import com.gjfax.app.ui.utils.TimeUtil;
import com.igexin.sdk.PushManager;

/**
 * @ClassName: SplashActivity
 * @Description:APP启动页面
 * @Create by 罗旭东 Date 2013-8-06 上午10:10:44
 */
public class SplashActivity extends BaseActivity {
	private static final int REQUEST_CODE_GUIDE = 1;

	private static final int MSG_REDIRECT_LOGIN = 0x101;

	private static final int MSG_REDIRECT_MAIN = 0x102;

	private ImageView mSplashImgImageView = null;

	private Bitmap mSplashImgBitmap = null;

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REDIRECT_LOGIN:
			redirectLogin();
			break;
		case MSG_REDIRECT_MAIN:
			redirectMain();
			break;
		default:
			break;
		}
	}

	@Override
	protected int getContentViewLayoutResId() {
		return R.layout.activity_splash;
	}

	@Override
	protected void onCreateFindView(Bundle savedInstanceState) {
		mSplashImgImageView = (ImageView) findViewById(R.id.iv_splash);
	}

	@Override
	protected void onCreateAddListener(Bundle savedInstanceState) {
	}

	@Override
	protected void onCreateInitData(Bundle savedInstanceState) {
		PushManager.getInstance().initialize(this.getApplicationContext());

		if (!TextUtils.isEmpty(ConfigUtil.getSplashImgPath())) {
			File file = new File(ConfigUtil.getSplashImgPath());

			if (file.exists()) {
				try {
					FileInputStream fis = new FileInputStream(file);
					mSplashImgBitmap = BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

				} catch (FileNotFoundException e) {
				}
				mSplashImgImageView.setImageBitmap(mSplashImgBitmap);
			}
		}
	}

	@Override
	protected void onCreateTaskLoadData() {
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}

		// if (!ConfigUtil.isShowGuide()){
		// startActivityForResult(new Intent(this, GuideActivity.class),
		// REQUEST_CODE_GUIDE);
		// }else{
		sendMessage(obtainMessage(MSG_REDIRECT_MAIN, null));
		// }
	}

	@Override
	protected void onCreateTaskAddView() {

	}

	private void redirectLogin() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_GUIDE:
			sendMessage(obtainMessage(MSG_REDIRECT_MAIN, null));
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (mSplashImgBitmap != null) {
			mSplashImgBitmap.recycle();
		}
		super.onDestroy();
	}

	private void redirectMain() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_RECOMMEND_PRODUCT, mRecommendProduct);
		intent.putExtra(MainActivity.EXTRA_NEW_USER_PRODUCT, mNewUserProduct);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		finish();
	}
}

/**
 * Title: MainActivity.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年7月28日 下午2:21:48
 * Version: 1.0
 */
package com.gjfax.app.ui.activities;

import java.io.File;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjfax.app.R;
import com.gjfax.app.logic.common.BaseConstant;
import com.gjfax.app.logic.common.values.BannerType;
import com.gjfax.app.logic.device.DeviceFactory;
import com.gjfax.app.logic.device.manager.DeviceCallable;
import com.gjfax.app.logic.network.http.model.vo.GetBannerListRet;
import com.gjfax.app.logic.upgrade.UpgradeManagerFactory;
import com.gjfax.app.logic.upgrade.manager.UpgradeCallable;
import com.gjfax.app.logic.user.UserFactory;
import com.gjfax.app.logic.utils.ConfigUtil;
import com.gjfax.app.logic.utils.GlobalUtil;
import com.gjfax.app.logic.utils.MetaDataUtil;
import com.gjfax.app.ui.fragments.BaseFragment;
import com.gjfax.app.ui.fragments.InvestFragment;
import com.gjfax.app.ui.fragments.MoreFragment;
import com.gjfax.app.ui.fragments.MyAssetsFragment;
import com.gjfax.app.ui.fragments.RecommendFragment;
import com.gjfax.app.ui.utils.ToastUtil;
import com.gjfax.app.ui.utils.UserUtil;
import com.gjfax.app.ui.utils.ViewUtil;
import com.gjfax.app.ui.widgets.GjfaxDialog;
import com.gjfax.app.ui.widgets.GuidingDialog;
import com.luoxudong.app.asynchttp.AsyncHttpUtil;
import com.luoxudong.app.asynchttp.callable.DownloadRequestCallable;
import com.luoxudong.app.utils.LogUtil;
import com.luoxudong.app.utils.MD5;
import com.luoxudong.app.utils.PackageUtil;
import com.networkbench.agent.impl.NBSAppAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * ClassName: MainActivity
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by: 罗旭东
 * Date: 2015年7月28日 下午2:21:48
 * </pre>
 */
public class MainActivity extends FragmentActivity {
	private final static int REQUEST_CODE_LOGIN = 1;

	private final static String VERSION_KEY = "version_key";

	public final static int TAB_ID_RECOMMEND = R.id.rb_recommend;

	public final static int TAB_ID_INVEST = R.id.rb_invest;

	public final static int TAB_ID_MY_ASSET = R.id.rb_my_assets;

	public final static int TAB_ID_MORE = R.id.rb_more;

	/** 推荐出产品信息 */
	public final static String EXTRA_RECOMMEND_PRODUCT = "recommendProduct";

	/** 新手理财产品 */
	public final static String EXTRA_NEW_USER_PRODUCT = "newUserProduct";

	public static MainActivity sMainActivity = null;

	/** 底部tab栏 */
	private RadioGroup mBottomRadioGroup = null;

	private FragmentManager mFragmentManager = null;

	private SparseArray<BaseFragment> mFragmentSparseArray = null;

	/** 当前fragment */
	private BaseFragment mCurrentFragment = null;

	/** 上一个fragment */
	private BaseFragment mLastFragment = null;

	private RadioButton[] mRadioButtons = null;

	/** tab显示名称 */
	private String[] mRadioButtonTexts = null;

	/** radioButton的id */
	private int[] mRadioButtonIds = new int[] { R.id.rb_recommend,
			R.id.rb_invest, R.id.rb_my_assets, R.id.rb_more };

	/** tab的图标 */
	private int[] mRadioButtonImageIds = new int[] {
			R.drawable.btn_main_tabbar_recommend,
			R.drawable.btn_main_tabbar_invest,
			R.drawable.btn_main_tabbar_my_assets,
			R.drawable.btn_main_tabbar_more };

	@SuppressWarnings("unchecked")
	private Class<? extends BaseFragment>[] mTargetFragments = new Class[] {
			RecommendFragment.class, InvestFragment.class,
			MyAssetsFragment.class, MoreFragment.class };

	private long mTimestamp = 0;

	/** 我的资产layout */
	private RelativeLayout mMyAssetsLayout = null;

	/** 更多layout */
	private RelativeLayout mMoreLayout = null;

	/** 当前选中的tab */
	private int mCheckedId = 0;

	/** 引导框 */
	private GuidingDialog mGuidingDialog = null;

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_recommend:
				MobclickAgent.onEvent(MainActivity.this, "E_ID_RECOMMEND_TAB");
				break;
			case R.id.rb_invest:
				MobclickAgent.onEvent(MainActivity.this, "E_ID_INVEST_TAB");
				break;
			case R.id.rb_my_assets:
				if (UserUtil.checkUserLoginState(MainActivity.this,
						REQUEST_CODE_LOGIN)) {
					MobclickAgent.onEvent(MainActivity.this,
							"E_ID_MY_ASSETS_TAB");
				} else {
					return;
				}
				break;
			case R.id.rb_more:
				MobclickAgent.onEvent(MainActivity.this, "E_ID_MY_MORE_TAB");
				break;
			default:
				break;
			}

			changeTab(checkedId);
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
		MobclickAgent.onResume(this);
		LogUtil.d(this.toString(),
				"====sServerVersionNo = " + ConfigUtil.getServerVersionNo()
						+ " ,getVersionCode = "
						+ PackageUtil.getVersionCode(this));
		if (ConfigUtil.getNeedShowRedHotForActivity()
				|| ConfigUtil.getNeedShowRedHotForMsg()
				|| ConfigUtil.getNeedShowRedHotForNews()
				|| ConfigUtil.getServerVersionNo() > PackageUtil
						.getVersionCode(this)) {
			mMoreLayout.getChildAt(0).setVisibility(View.VISIBLE);
		} else {
			mMoreLayout.getChildAt(0).setVisibility(View.GONE);
		}
	}

	private boolean isFirstIn() {
		// TODO Auto-generated method stub
		int currentVersion = 0;
		try {
			PackageInfo pi = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			currentVersion = pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		int lastVersion = prefs.getInt(VERSION_KEY, 0);
		if (currentVersion > lastVersion) {
			// 如果当前版本大于上次版本，该版本属于第一次启动
			// 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
			prefs.edit().putInt(VERSION_KEY, currentVersion).commit();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - mTimestamp > BaseConstant.EXIT_APP_CLICK_TIME_INTERVAL * 1000) {
				mTimestamp = System.currentTimeMillis();
				ToastUtil.showToast(this, R.string.common_click_exit);
				return true;
			}
			UserFactory.getUserManager().releaseUserData();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		onCreateFindView(savedInstanceState);

		onCreateAddListener(savedInstanceState);

		onCreateInitData(savedInstanceState);

		sMainActivity = this;

		if (isFirstIn()) {
			mGuidingDialog = new GuidingDialog(this);
			mGuidingDialog.setCanceledOnTouchOutside(false);
			mGuidingDialog.setCancelable(false);
			mGuidingDialog.show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_LOGIN:
			switch (resultCode) {
			case LoginActivity.RESULT_CODE_LOGIN_SUC:
				changeTab(R.id.rb_my_assets);
				mCurrentFragment
						.onActivityResult(requestCode, resultCode, data);
				break;
			default:
				((RadioButton) mBottomRadioGroup.findViewById(mCheckedId))
						.setChecked(true);
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * actionbar 点击事件
	 */
	public void onClick(View v) {
		mCurrentFragment.onClick(v);// 点击事件传递给子fragment
	}

	/**
	 * 更新UI
	 */
	public void updateUI() {
		if (GlobalUtil.sShowUnreadRewardNum) {
			TextView tipTextView = (TextView) mMyAssetsLayout.getChildAt(0);
			tipTextView.setVisibility(View.VISIBLE);
			tipTextView.setText(String.valueOf(GlobalUtil.sUnreadRewardNum));
		} else {
			mMyAssetsLayout.getChildAt(0).setVisibility(View.GONE);
		}
	}

	/**
	 * 切换TAB
	 * 
	 * @param tabId
	 */
	public void swichTab(int tabId) {
		((RadioButton) mBottomRadioGroup.findViewById(tabId)).setChecked(true);
	}

	/**
	 * 初始化tab页面数据
	 */
	public void initTabsData() {
		if (mFragmentSparseArray != null) {
			for (int nIndex = 0; nIndex < mFragmentSparseArray.size(); nIndex++) {
				BaseFragment fragment = mFragmentSparseArray
						.get((Integer) mRadioButtonIds[nIndex]);
				fragment.refreshUI(null);
			}
		}

		swichTab(TAB_ID_RECOMMEND);
		BaseActivity.releaseAllActivities();
		updateUI();
	}

	protected void onCreateFindView(Bundle savedInstanceState) {
		mBottomRadioGroup = (RadioGroup) findViewById(R.id.rg_main_tab);
		mMyAssetsLayout = (RelativeLayout) findViewById(R.id.rl_my_assets);
		mMoreLayout = (RelativeLayout) findViewById(R.id.rl_more);
	}

	protected void onCreateAddListener(Bundle savedInstanceState) {
		mBottomRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
	}

	protected void onCreateInitData(Bundle savedInstanceState) {
		NBSAppAgent.setLicenseKey(MetaDataUtil.getTingYunAppKey(this))
				.withLocationServiceEnabled(true).start(this);
		;

		mFragmentManager = getSupportFragmentManager();
		mFragmentSparseArray = new SparseArray<BaseFragment>();
		mRadioButtonTexts = getResources().getStringArray(
				R.array.mainBarRadioGroup);

		mRadioButtons = new RadioButton[mRadioButtonIds.length];

		try {
			for (int nIndex = 0; nIndex < mRadioButtonIds.length; nIndex++) {
				mRadioButtons[nIndex] = (RadioButton) mBottomRadioGroup
						.findViewById(mRadioButtonIds[nIndex]);
				mRadioButtons[nIndex].setText(mRadioButtonTexts[nIndex]);
				BaseFragment fragment = mTargetFragments[nIndex].newInstance();
				mFragmentSparseArray.put(mRadioButtonIds[nIndex], fragment);
				if (mRadioButtonImageIds != null) {
					Drawable drawable = getResources().getDrawable(
							mRadioButtonImageIds[nIndex]);
					mRadioButtons[nIndex]
							.setCompoundDrawablesWithIntrinsicBounds(null,
									drawable, null, null);
				}

				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);

				int tabWidth = dm.widthPixels / mRadioButtonIds.length;
				int tabHeight = ViewUtil.getViewHeight(mBottomRadioGroup);
				Drawable[] drawables = mRadioButtons[nIndex]
						.getCompoundDrawables();
				int drawableWidth = drawables[1].getIntrinsicWidth();
				int drawableHeight = drawables[1].getIntrinsicHeight();
				drawableWidth = (tabWidth + drawableWidth) / 2;
				int posX = drawableWidth
						- getResources().getDimensionPixelOffset(
								R.dimen.new_msg_tip_padding_l);
				int posY = ViewUtil.getViewHeight(mRadioButtons[nIndex]);

				posY = (tabHeight - drawableHeight)
						/ 2
						- getResources().getDimensionPixelOffset(
								R.dimen.new_msg_tip_padding_t);
				mMyAssetsLayout.setPadding(posX, posY, 0, 0);
				mMoreLayout.setPadding(posX, posY, 0, 0);

				mMyAssetsLayout.getChildAt(0).setVisibility(View.GONE);
				// mMoreLayout.getChildAt(0).setVisibility(View.GONE);
			}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		mLastFragment = mCurrentFragment = mFragmentSparseArray
				.get((Integer) mRadioButtonIds[0]);
		mCheckedId = R.id.rb_recommend;
		ft.add(R.id.main_content, mCurrentFragment);
		ft.commitAllowingStateLoss();

		// 自动登录
		// autoLogin();

		// 升级检测
		updateCheck();

		// 更新启动页图片
		updateSplashPic();
	}

	private void changeTab(int tabId) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		mCurrentFragment = mFragmentSparseArray.get((Integer) tabId);

		if (mCurrentFragment == mLastFragment) {
			return;
		}

		if (!mCurrentFragment.isAdded()) {// fragment没有添加则添加
			ft.add(R.id.main_content, mCurrentFragment);
		}

		ft.hide(mLastFragment);
		ft.show(mCurrentFragment);
		mLastFragment = mCurrentFragment;
		ft.commitAllowingStateLoss();
		mCurrentFragment.showFragment();

		mCheckedId = tabId;
	}

	/**
	 * 检测升级
	 */
	private void updateCheck() {
		UpgradeManagerFactory.getUpgradeManager().upgradeCheck(this,
				new UpgradeCallable() {

					@Override
					public void onFindNewVersion(final String url,
							String versionName, String appName, String desc,
							String md5, int upgradeLevel, final boolean isForce) {

						GjfaxDialog.Builder builder = new GjfaxDialog.Builder(
								MainActivity.this);
						builder.setTitle(getResources().getString(
								R.string.software_upgrade));
						builder.setBtnText(getResources().getString(R.string.software_upgrade_btn));
						if (isForce){
							builder.setMessage(getString(R.string.upgrade_right_now_force_tip));
						}else{
							builder.setMessage(getString(R.string.upgrade_right_now_tip));
						}
						
						builder.setCancelable(false);
						builder.setCanceledOnTouchOutside(false);
						builder.setDismiss(false);
						builder.setPositiveButton(new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent();
								intent.setAction("com.gjfax.app.ACTION_DOWNLOAD_APP");
								intent.putExtra("url", url);
								sendBroadcast(intent);
								MobclickAgent.onEvent(MainActivity.this,
										"E_ID_MAIN_UPDATE");
								
								if (!isForce) {
									dialog.dismiss();
								}else{
									ToastUtil.showToast(MainActivity.this, "正在下载升级包...");
									finish();
								}
							}
						});

						builder.setNegativeButton(new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 取消
								if (isForce) {
									finish();
								}								
							}
						});
						builder.create().show();

					}

					@Override
					public void onUnFindNewVersion() {

					}

					@Override
					public void onCallbackFail(int errorCode, String msg) {

					}
				});
	}

	/**
	 * 自动登录
	 */
	private void autoLogin() {
		if (ConfigUtil.isAutoLogin()
				&& !TextUtils.isEmpty(ConfigUtil.getUserId())
				&& !TextUtils.isEmpty(ConfigUtil.getPassword())) {
			UserFactory.getUserManager().loginRequest(this,
					ConfigUtil.getUserId(), ConfigUtil.getPassword(), null);
		}
	}

	/**
	 * 更新启动页图片
	 */
	private void updateSplashPic() {
		DeviceFactory.getDeviceManager().getBannerListRequest(this,
				BannerType.splash.getValue(), new DeviceCallable() {

					@Override
					public void onCallbackFail(int errorCode, String msg) {

					}

					@Override
					public void getBannerList(List<GetBannerListRet> list) {
						if (list != null && list.size() > 0) {
							String url = list.get(0).getSaveURL();
							if (!TextUtils.isEmpty(url)) {
								String fileName = MD5.hexdigest(url);
								if (url.indexOf(".jpg") > 0) {
									fileName += ".jpg";
								} else {
									fileName += ".png";
								}

								final File file = new File(
										BaseConstant.USER_CACHE_IMAGE, fileName);

								if (!file.exists()
										|| TextUtils.isEmpty(ConfigUtil
												.getSplashImgPath())) {
									AsyncHttpUtil.download(list.get(0)
											.getSaveURL(),
											BaseConstant.USER_CACHE_IMAGE,
											fileName,
											new DownloadRequestCallable() {

												@Override
												public void onFailed(int arg0,
														String arg1) {
													file.delete();
													ConfigUtil
															.setSplashImgPath(null);
												}

												@Override
												public void onSuccess(
														String responseInfo) {
													ConfigUtil
															.setSplashImgPath(file
																	.getAbsolutePath());
												}
											});
								}
							}
						}
					}
				});
	}
}

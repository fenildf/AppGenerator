/**
 * Title: BaseFragment.java
 * Description:
 * Copyright: Copyright (c) 2013 luoxudong.com
 * Company:个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-9-4 下午4:37:44
 * Version 1.0
 */
package com.gjfax.app.ui.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gjfax.app.R;
import com.gjfax.app.ui.widgets.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

/** 
 * ClassName: BaseFragment
 * Description:Fragment基础类
 * Create by 罗旭东
 * Date 2013-9-4 下午4:37:44
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseFragment extends Fragment{
	/** 初始化消息,数值取大一些，避免跟子类中的消息冲突 */
	private static final int MSG_INIT = Integer.MAX_VALUE - 1;
	
	/** 网络异常 */
	public static final int MSG_NETWORK_INVALID = Integer.MAX_VALUE - 2;
	
	/** sd卡不可用 */
	protected static final int MSG_SDCARD_UN_READY = Integer.MAX_VALUE - 3;
	
	/** 刷新UI */
	protected static final int MSG_REFRESH_UI = Integer.MAX_VALUE - 4;
	
	/** 错误信息 */
	protected static final int MSG_EXCEPTION = Integer.MAX_VALUE - 5;
	
	/** 最外层布局 */
	protected View mMainLayout = null;
	
	/** 是否初始化 */
	protected boolean mIsInited = false;
	
	/** 标题栏 */
	protected View mTitleView = null;
	
	/** 加载框控件 */
	private LoadingDialog mLoadingDialog = null;
	
	/**
	 * Fragment布局文件ID
	 * @return 布局文件ID
	 */
	protected abstract int getFragmentLayoutResId();
	
	/**
	 * 初始化界面 （加载控件View对象）
	 *
	 */
	protected abstract void onCreateFindView(Bundle savedInstanceState);

	/**
	 * 加载操作事件（View控件的点击事件响应等，控件的监听方法）
	 *
	 */
	protected abstract void onCreateAddListener(Bundle savedInstanceState);

	/**
	 * 放入初始化的数据(onCreate主线程加载处理，用于不耗时操作或起线程操作)
	 *
	 */
	protected abstract void onCreateInitData(Bundle savedInstanceState);

	/**
	 * 线程加载数据（用于后台处理耗时操作，主要用于数据库读取或发送请求操作，该方法中不能有UI操作，有UI操作会有不可预料的异常）
	 */
	protected abstract void onCreateTaskLoadData();

	/**
	 * 线程加载View（后台数据加载完后，用于界面View控件刷新操作，不能有后台耗时处理，会堵塞主线程）
	 */
	protected abstract void onCreateTaskAddView();
	
	/**
	 * 刷新UI
	 */
	public void refreshUI(Object obj){};
	
	private Handler baseHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INIT:// activity初始化
				InitTask initTask = new InitTask();
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
					initTask.execute();
				} else {
					initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}

				break;
			case MSG_NETWORK_INVALID:// 网络异常
				break;
			case MSG_SDCARD_UN_READY:// SD卡不存在
				break;
			case MSG_REFRESH_UI:// 刷新UI
				refreshUI(msg.obj);
				break;
			case MSG_EXCEPTION:// 错误信息
				break;

			default:
				super.handleMessage(msg);
				break;
			}

			BaseFragment.this.handleMessage(msg);
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
		if (isVisible()){
			showFragment();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
		if (isVisible()){
			hiddenFragment();
		}
	}
	

	protected int getStyle(){
		return 0;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		LayoutInflater localInflater = null;
		if (getStyle() != 0){
			Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), getStyle());
			localInflater = inflater.cloneInContext(contextThemeWrapper);
		}else {
			localInflater = inflater;
		}
		
		if (getFragmentLayoutResId() != 0) {
			mMainLayout = localInflater.inflate(getFragmentLayoutResId(), null);
		} else {
			mMainLayout = initDefaultView();
		}
		
		initTitleView();
		
		onCreateFindView(savedInstanceState);
		
		onCreateAddListener(savedInstanceState);
		
		mIsInited = true;
		return mMainLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onCreateInitData(savedInstanceState);
		sendMessage(obtainMessage(MSG_INIT, null));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	protected void handleErrorMsg(int errorCode, String errorMsg){}
	
	/**
	 * 
	 * @description:消息发送给子类
	 * @param msg 消息
	 * @return void
	 * @throws
	 */
	protected void handleMessage(Message msg) {
	}
	
	/**
	 * 显示fragment
	 */
	public void showFragment(){}
	
	/**
	 * 自动下拉刷新
	 * 
	 */
	protected void onRefreshing(){};
	
	/**
	 * 显示同步信息
	 * 
	 */
	protected void showSyncInfo(String msg){};
	
	/**
	 * 隐藏同步信息
	 */
	protected void hiddenSyncInfo(){};
	
	/**
	 * 隐藏fragment
	 * 
	 */
	public void hiddenFragment(){}
	
	protected Message obtainMessage(int what, Object obj) {
        Message msg = null;
        if(baseHandler != null){
            msg = baseHandler.obtainMessage(what, obj);
        }else{
            msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
        }
        return msg;
    }
	
	protected void sendMessage(Message msg) {
        if(baseHandler != null){
        	baseHandler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

	protected void sendMessage(Message msg, long delayMillis) {
        if(baseHandler != null){
        	baseHandler.sendMessageDelayed(msg, delayMillis);
        } else {
            handleMessage(msg);
        }
    }
	
	/**
	 * 根据ID获取资源
	 * @param id
	 * @return View
	 */
	protected View findViewById(int id)
	{
		if (mMainLayout != null)
		{
			return mMainLayout.findViewById(id);
		}
		return null;
	}
	
	protected View initDefaultView()
	{
		TextView textView = new TextView(getActivity());
		textView.setText("没有指定控件");
		return textView;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return false;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		return false;
	}
	
	private class InitTask extends AsyncTask<String, Integer, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params)
		{
			onCreateTaskLoadData();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			onCreateTaskAddView();
		}
		
	}
	
	public Handler getBaseHandler() {
		return baseHandler;
	}

	public void setBaseHandler(Handler baseHandler) {
		this.baseHandler = baseHandler;
	}
	
	/**
	 * 初始化actionbar
	 */
	protected void initTitleView(){
		if (getActivity() == null){
			return;
		}
		
		if (mMainLayout != null){
			mTitleView = (View)mMainLayout.findViewById(R.id.layout_title);
		}
	}
	
	/**
	 * actionbar 点击事件
	 */
	public void onClick(View v) {
		
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	protected void setTitle(String title){
		if (mTitleView != null){
			TextView titleTextView = (TextView)mTitleView.findViewById(R.id.tv_title);
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(title);
		}
	}
	
	/**
	 * 默认标题栏
	 */
	protected void initDefaultTitle(){
		if (mTitleView != null){
			mTitleView.findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
			mTitleView.findViewById(R.id.layout_title_style_1).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_2).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_3).setVisibility(View.GONE);
			
			((TextView)mTitleView.findViewById(R.id.tv_title)).setText(getResources().getString(R.string.app_name));
			
		}
	}
	
	/**
	 * 返回按钮样式
	 */
	protected void initBackBtnTitle(){
		if (mTitleView != null){
			mTitleView.findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
			mTitleView.findViewById(R.id.layout_title_style_1).setVisibility(View.VISIBLE);
			mTitleView.findViewById(R.id.layout_title_style_2).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_3).setVisibility(View.GONE);
		}
	}
	
	/**
	 * 返回按钮以及右侧文字按钮
	 */
	protected void initBackAndRightTextBtnTitle(String rightText){
		if (mTitleView != null){
			mTitleView.findViewById(R.id.tv_title).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_1).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_2).setVisibility(View.VISIBLE);
			mTitleView.findViewById(R.id.layout_title_style_3).setVisibility(View.GONE);
			
			if (!TextUtils.isEmpty(rightText)){
				Button textView = (Button)mTitleView.findViewById(R.id.layout_title_style_2).findViewById(R.id.btn_title_right_2);
				textView.setText(rightText);
			}
			
		}
	}
	
	/**
	 * 首页标题样式
	 */
	protected void initMainTitle(){
		if (mTitleView != null){
			mTitleView.findViewById(R.id.tv_title).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_1).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_2).setVisibility(View.GONE);
			mTitleView.findViewById(R.id.layout_title_style_3).setVisibility(View.VISIBLE);
		}
	}
	
	
	protected void setMainTitleRightView(boolean visible){
		if (mTitleView != null){
			if (visible){
				mTitleView.findViewById(R.id.btn_title_right_3).setVisibility(View.VISIBLE);
			}else{
				mTitleView.findViewById(R.id.btn_title_right_3).setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 显示等待框
	 * 
	 * @param title
	 */
	protected void showLoadingDialog(String msg) {
		showLoadingDialog(msg, false);
	}
	
	/**
	 * 显示等待框
	 * @param msg
	 * @param cancel 是否可取消
	 */
	protected void showLoadingDialog(String msg, boolean cancel) {
		mLoadingDialog = new LoadingDialog(getActivity());
		mLoadingDialog.setCanceledOnTouchOutside(false);
		mLoadingDialog.setCancelable(cancel);
		mLoadingDialog.setMessage(msg);
		mLoadingDialog.show();
	}

	/**
	 * 隐藏等待框
	 */
	protected void hiddenLoadingDialog() {
		if (mLoadingDialog != null) {
			mLoadingDialog.dismiss();
		}
	}
	
	/**
	 * 是否点击外面可以取消
	 * @param cancel
	 */
	protected void setCanceledOnTouchOutside(boolean cancel){
		if (mLoadingDialog != null){
			mLoadingDialog.setCanceledOnTouchOutside(cancel);
		}
	}
	
	/**
	 * 是否可取消
	 * @param flag
	 */
	protected void setCancelable(boolean flag){
		if (mLoadingDialog != null){
			mLoadingDialog.setCancelable(flag);
		}
	}
}

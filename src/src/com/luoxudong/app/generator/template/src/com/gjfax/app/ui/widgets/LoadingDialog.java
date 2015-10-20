/**
 * Title: LoadingDialog.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年8月23日 下午4:57:50
 * Version: 1.0
 */
package com.gjfax.app.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjfax.app.R;

/** 
 * <pre>
 * ClassName: LoadingDialog
 * Description:弹出式加载框
 * Create by: 罗旭东
 * Date: 2015年8月23日 下午4:57:50
 * </pre>
 */
public class LoadingDialog extends Dialog {
	private ImageView mIconImageView = null;
	
	private TextView mMsgTextView = null;

	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialog);
		setContentView(R.layout.layout_loading_dialog);
		mIconImageView = (ImageView) findViewById(R.id.iv_loading_dialog);
		mMsgTextView = (TextView) findViewById(R.id.tv_loading_dialog);
		getWindow().getAttributes().alpha = 0.8f;
	}

	protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoadingDialog setMessage(String msg) {
		if (mMsgTextView != null && !TextUtils.isEmpty(msg)) {
			mMsgTextView.setText(msg);
		}
		return this;
	}

	public LoadingDialog setMessage(int msgResId) {
		if (mMsgTextView != null) {
			mMsgTextView.setText(msgResId);
		}
		return this;
	}
	
	@Override
	public void show() {
		RotateYAnimation animation = new RotateYAnimation();
		animation.setFillAfter(true);
		animation.setRepeatCount(Animation.INFINITE);
		mIconImageView.startAnimation(animation);
		super.show();
	}
}

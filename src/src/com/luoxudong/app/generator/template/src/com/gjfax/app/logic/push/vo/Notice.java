package com.gjfax.app.logic.push.vo;

import java.io.Serializable;

public class Notice implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 通知类型（0：普通通知；1：升级通知；2：活动中心；3:：消息中心；4：最新动态；  */
	private int noticeType = 0;

	/** 标题 */
	private String title = null;

	/** 内容 */
	private String content = null;
	
	/** 通知栏上显示的icon */
	private String iconUrl = null;
	
	/** 不为空时，点击内容要跳转的url */
	private String redirectUrl = null;
	
	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}

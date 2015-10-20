package com.gjfax.app.ui.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

	/** 身份证 */
	private static final String V_IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

	public static String join(Collection<?> coll, String join) {
		if (coll == null || coll.isEmpty())
			return "";
		StringBuffer sb = new StringBuffer();
		Iterator<?> it = coll.iterator();
		if (it.hasNext())
			sb.append(it.next());
		while (it.hasNext()) {
			sb.append(join + it.next());
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否为null或者空字符串
	 * 
	 * @param input
	 *            输入的字符串
	 * @return 如果为null或者空字符串，返回true；否则返回false
	 */
	public static boolean isNullOrEmpty(String input) {
		if (null == input || input.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证是否为手机号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 验证是不是正确的身份证号码
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IDcard(String value) {
		return match(V_IDCARD, value);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 日期显示转换 例如:将20150807转换成:2015-08-07
	 * 
	 * @param str
	 * @return str
	 */
	public static String parseDate(String date) {
		String newDate = null;
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		newDate = year + "-" + month + "-" + day;
		return newDate;
	}

	/**
	 * 手机号码隐藏中间4位的显示 例如:将13820150807转换成:138****0807
	 * 
	 * @param str
	 * @return str
	 */

	public static String parsePhone(String phone) {
		String str = phone.substring(3, 7);
		String replace = phone.replace(str, "****");
		return replace;
	}

	/**
	 * 判断字符串是否为空或为空字符串
	 */
	public static String checkFiledIsNull(String filed) {
		if (filed == null || filed.equals("")) {
			return "0";
		}
		return filed;
	}

	/**
	 * 广金币去掉小数点之后的数字
	 * 
	 * @param money
	 * @return
	 */
	public static String parseGjfaxCoin(String money) {
		if (money != null && money.indexOf(".") != -1) {
			money = money.substring(0, money.indexOf("."));
		}
		return money;
	}
}

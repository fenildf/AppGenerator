package com.gjfax.app.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	/**
	 * 比较两个时间的先后
	 * 
	 * @param d1
	 *            格式为2015-07-02 18:50:54
	 * @param d2
	 *            格式为2015-07-02 18:50:54
	 * @return d1早返回1，d2早返回-1，相同返回0
	 * 
	 */
	public static int getTimeEarlier(String d1, String d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = sdf.parse(d1);
			Date date2 = sdf.parse(d2);

			if (date1.getTime() < date2.getTime())
				return 1;
			else if (date1.getTime() > date2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}
}

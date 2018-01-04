package com.ltsznh.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
	public final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	public static String getRandomIdString() {
		// 生成唯一ID,随机数加一，确保随机数位数相同，否则小于0.1的缺位数
		return sdf.format(new Date())
				+ ((long) ((Math.random() + 1) * 1000L) - 1000);
	}
}

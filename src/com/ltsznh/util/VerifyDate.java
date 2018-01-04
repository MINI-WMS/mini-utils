package com.ltsznh.util;

import javafx.util.converter.LocalDateStringConverter;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @version 2014/12/21
 * 判断日期是否符合指定格式
 */
public class VerifyDate {
	private static DateFormat dateFormat = DateFormat.getDateInstance();

//	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyyMMdd");
//	private static DateTimeFormat dateFormat1 = DateTimeFormat.getFormat("yyyy/MM/dd");
//	private static DateTimeFormat dateFormat2 = DateTimeFormat.getFormat("yyyy-MM-dd");

	/**
	 * 校验日期是否为日期格式
	 * @param date
	 * @return
	 */
	public static boolean isDate(LocalDate date){
		if(date == null)return false;
		try{
			new LocalDateStringConverter().fromString(date.toString());
			return true;
		}catch (Exception e){
			return false;
		}
	}

	/**
	 * @param date 要验证的日期
	 * @param allowedNull 是否允许为空
	 * @return
	 */
	public static boolean verify(String date,boolean allowedNull) {
		if(allowedNull&&date.equals("")){
			return true;
		}
		return false;
//		try {
//			date = date.replace("/", "").replace("-", "").replace("-", "");
////			dateFormat.format
//			dateFormat.parse(date);
//			return true;
//		} catch (Exception e) {
//			// TODO: handle exception
////			Window.alert(e.getMessage() + "");
//			return false;
//		}
	}
	
	
	public static Date getDate(String date){
	try {
			date = date.replace("/", "").replace("-", "");
		return dateFormat.parse(date);
		} catch (Exception e) {
			// TODO: handle exception
//			Window.alert(e.getMessage() + "");
			return new Date();
		}
		
		
		
	}
}

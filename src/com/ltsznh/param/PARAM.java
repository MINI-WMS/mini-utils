package com.ltsznh.param;

import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PARAM {

	public static final String VERSION_STRING = "2016-12-30";

	public static final String ENCODING = "UTF-8";


	// 与WEB端交互变量
	private static String BASE_URL = "http://localhost:8080";// WEB地址
	public static String getBaseUrl() {
		return BASE_URL;
	}

	public static String getCOOKIES() {
		return COOKIES;
	}

	//	private static String COOKIES = null;// WEB端SESSION值
	private static String COOKIES = "JSESSIONID=3ba6e5f0-140c-4da9-ab8d-46877dc74306;rememberMe=deleteMe;";// WEB端SESSION值
	/**
	 * 设置Cookies
	 *
	 * @param COOKIES
	 */
	public static void setCOOKIES(String COOKIES) {
		if (COOKIES != null) PARAM.COOKIES = COOKIES;
		if (PARAM.COOKIES == null) return;
		// 设置cookies
		new WebView();// 初始化一个WebView，否则CookieHandler.getDefault()为null
		URI uri = URI.create(PARAM.getBaseUrl());
		Map<String, List<String>> headers = new LinkedHashMap<>();
		headers.put(Const.KEY_HEADER_COOKIE, Arrays.asList(PARAM.getCOOKIES()));
		try {
			CookieHandler.getDefault().put(uri, headers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

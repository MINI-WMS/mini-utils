package com.ltsznh.util;

public class bWindow {

	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;
}

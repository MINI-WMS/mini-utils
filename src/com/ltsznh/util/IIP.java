package com.ltsznh.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 涛 on 2015/07/28
 */
public class IIP {
	public String getIP() {
		// TODO Auto-generated method stub
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ip = addr.getHostAddress().toString();// 获得本机IP
		return ip;
	}

}

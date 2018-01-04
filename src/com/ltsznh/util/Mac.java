package com.ltsznh.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by 涛 on 2015/07/28
 */
public class Mac {
	public static void main(String[] arguments) throws Exception {
		InetAddress ia = InetAddress.getLocalHost();// 获取本地IP对象
		System.out.println("MAC:" + new Mac().getMACAddress(ia));
	}

	// 获取MAC地址的方法
	public String getMACAddress(InetAddress ia) {
		byte[] mac = null;
		if (ia == null)
			try {
				ia = InetAddress.getLocalHost();
				// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
				mac = NetworkInterface.getByInetAddress(ia)
						.getHardwareAddress();
			} catch (UnknownHostException | SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}// 获取本地IP对象

		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase();
	}
}

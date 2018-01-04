package com.ltsznh.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 涛 on 2015/07/28
 */
public class IP {
	public static String getPIP(HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 跟踪IP
		// String ip = "";
		// if(request.getHeader("x-forwarded-for") != null){
		// ip += request.getHeader("x-forwarded-for");
		// }
		// if(request.getHeader("Proxy-Client-IP") != null){
		// ip += " - " +request.getHeader("Proxy-Client-IP");
		// }
		// if(request.getHeader("WL-Proxy-Client-IP") != null){
		// ip += " - " +request.getHeader("WL-Proxy-Client-IP");
		// }
		// if(request.getRemoteAddr() != null){
		// ip += " - " +request.getRemoteAddr();
		// }
		String ip = "";

		String xip = request.getHeader("x-forwarded-for");
		ip = addIp(ip, xip);
		xip = request.getHeader("Proxy-Client-IP");
		ip = addIp(ip, xip);
		xip = request.getHeader("WL-Proxy-Client-IP");
		ip = addIp(ip, xip);
		xip = request.getRemoteAddr();
		ip = addIp(ip, xip);
		return ip;

		// logger.info("TTTTT", "", "getRemoteAddr"
		// + getThreadLocalRequest().getRemoteAddr() + "getRemoteHost"
		// + getThreadLocalRequest().getRemoteHost() + "getRemotePort"
		// + getThreadLocalRequest().getRemotePort() + "getRemoteUser"
		// + getThreadLocalRequest().getRemoteUser() + "\n" + ip);

		// System.out.println("Protocol: " + request.getProtocol());
		// System.out.println("Scheme: " + request.getScheme());
		// System.out.println("Server Name: " + request.getServerName() );
		// //获得服务器的名字
		// System.out.println("Server Port: " + request.getServerPort());
		// //获得服务器的端口号
		// System.out.println("rotocol: " + request.getProtocol());
		// System.out.println("Server Info: " +
		// getServletConfig().getServletContext().getServerInfo());
		// System.out.println("Remote Addr: " + request.getRemoteAddr());
		// //获得客户端的ip地址
		// System.out.println("Remote Host: " + request.getRemoteHost());
		// //获得客户端电脑的名字，若失败，则返回客户端电脑的ip地址
		// System.out.println("Character Encoding: " +
		// request.getCharacterEncoding());
		// System.out.println("Content Length: " + request.getContentLength());
		// System.out.println("Content Type: "+ request.getContentType());
		// System.out.println("Auth Type: " + request.getAuthType());
		// System.out.println("HTTP Method: " + request.getMethod());
		// //获得客户端向服务器端传送数据的方法有get、post、put等类型
		// System.out.println("ath Info: " + request.getPathInfo());
		// System.out.println("ath Trans: " + request.getPathTranslated());
		// System.out.println("Query String: " + request.getQueryString());
		// System.out.println("Remote User: " + request.getRemoteUser());
		// System.out.println("Session Id: " + request.getRequestedSessionId());
		// System.out.println("Request URI: " +
		// request.getRequestURI());//获得发出请求字符串的客户端地址
		//
		// System.out.println("Servlet Path: " + request.getServletPath());
		// //获得客户端所请求的脚本文件的文件路径
		// System.out.println(request.getHeaderNames()); //返回所有request
		// header的名字，结果集是一个enumeration（枚举）类的实例
		// System.out.println("Accept: " + request.getHeader("Accept"));
		// System.out.println("Host: " + request.getHeader("Host"));
		// System.out.println("Referer : " + request.getHeader("Referer"));
		// System.out.println("Accept-Language : " +
		// request.getHeader("Accept-Language"));
		// System.out.println("Accept-Encoding : " +
		// request.getHeader("Accept-Encoding"));
		// System.out.println("User-Agent : " +
		// request.getHeader("User-Agent")); //返回客户端浏览器的版本号、类型
		// System.out.println("Connection : " +
		// request.getHeader("Connection"));
		// System.out.println("Cookie : " + request.getHeader("Cookie"));
		//
		// HttpSession session = request.getSession();
		//
		// System.out.println("Created : " + session.getCreationTime());
		// System.out.println("LastAccessed : " +
		// session.getLastAccessedTime());
	}

	private static String addIp(String ip, String xip) {
		if (xip != null && xip.length() > 0 && !"unknown".equalsIgnoreCase(xip)
				&& !"null".equalsIgnoreCase(xip)) {
			if (!ip.equals(""))
				ip += "->";
			ip += xip;
		}
		return ip;
	}
}

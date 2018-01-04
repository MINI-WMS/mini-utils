package com.ltsznh.util;

import java.io.*;

public class getHtmlTemplate {

	public String getHtml(String filePath) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if (file.exists())
			return getHtml(file);
		else
			return "";
	}

	public String getHtml(File file) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fis == null) {
			return "";
		} else {
			return getHtml(fis);
		}
	}

	private String getHtml(FileInputStream fis) {
		// TODO Auto-generated method stub
		StringBuffer html = new StringBuffer();
		InputStreamReader read;
		try {
			read = new InputStreamReader(fis, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				html.append(lineTxt);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 考虑到编码格式
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return html.toString();
	}
}

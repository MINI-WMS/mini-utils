package com.ltsznh.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;


import com.ltsznh.model.R;
import com.ltsznh.model.ResultData;
import com.ltsznh.param.PARAM;

import java.io.*;

/**
 * Created by 涛 on 2015/6/28.
 */
public class Convert {

	public static ResultData inputStreamToResultData(InputStream in) {
		return inputStreamToResultData(in, "utf-8");
	}

	public static ResultData inputStreamToResultData(InputStream in,
													 String encoding) {

		return new Gson().fromJson(inputStreamToString(in, encoding),
				ResultData.class);
	}

	public static R inputStreamToR(InputStream in) {
		return inputStreamToR(in, "utf-8");
	}

	public static R inputStreamToR(InputStream in,
								   String encoding) {
//		String resultString = inputStreamToString(in, encoding);
//		R result = JSON.parseObject(resultString, R.class);
//		return result;
		return JSON.parseObject(inputStreamToString(in, encoding), R.class);


//		return new Gson().fromJson(inputStreamToString(in, encoding),
//				R.class);
	}


	public static String inputStreamToString(InputStream in) {

		return inputStreamToString(in, PARAM.ENCODING);
	}

	public static String inputStreamToString(InputStream in, String encoding) {
		String result = "";
		BufferedReader reader = null;
		String lines = "";
		try {
			reader = new BufferedReader(new InputStreamReader(in, encoding));// 设置编码
			while ((lines = reader.readLine()) != null) {
				result = result + lines;
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String inputStreamToString(InputStreamReader in) {
		// TODO Auto-generated method stub
		return inputStreamToString(in, PARAM.ENCODING);
	}

	private static String inputStreamToString(InputStreamReader in,
											  String encoding) {
		// TODO Auto-generated method stub
		String result = "";
		BufferedReader reader = null;
		String lines = "";
		try {
			reader = new BufferedReader(in);// 设置编码
			while ((lines = reader.readLine()) != null) {
				result = result + lines;
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

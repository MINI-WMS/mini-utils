package com.ltsznh.util;

import com.google.gson.Gson;
import com.ltsznh.model.ResultData;
import com.ltsznh.param.PARAM;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by 涛 on 2015/6/30.
 */
public class getGsonString {

	public ResultData toGson(HashMap<String, Object> param) {
		ResultData result = new ResultData();
		if (param.containsKey("request"))
			param.remove("request");
		if (param.containsKey("response"))
			param.remove("response");

		try {
			String resultString = toGsonString(param);
			resultString = URLEncoder.encode(resultString, PARAM.ENCODING);
			result.setMessage(resultString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}

	public String toGsonString(HashMap<String, Object> params)
			throws UnsupportedEncodingException {

		if (params.containsKey("request"))
			params.remove("request");
		if (params.containsKey("response"))
			params.remove("response");
		return new Gson().toJson(params, HashMap.class);
	}
}

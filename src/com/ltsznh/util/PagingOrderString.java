package com.ltsznh.util;

import java.util.HashMap;

public class PagingOrderString {
	public static String getOrderString(HashMap<String, Object> param,
			String columNameString) {
		int col = Integer.parseInt(param.getOrDefault("orderBy", 0).toString());
		String orderByString = "";
		if (col > 0) {
			orderByString = columNameString;
			for (int i = 0; i < col; i++) {
				orderByString = orderByString.substring(orderByString
						.indexOf(",") + 1);
			}
			orderByString = orderByString.substring(0,
					orderByString.indexOf(","));
			if (orderByString.indexOf("AS") > 0)
				orderByString = orderByString.substring(0,
						orderByString.indexOf("AS"));
			if (orderByString.indexOf("as") > 0)
				orderByString = orderByString.substring(0,
						orderByString.indexOf("as"));
			if (!Boolean.parseBoolean(param.getOrDefault("isSortAscending",
					true).toString())) {
				orderByString += " DESC";
			}
			if (!orderByString.equals(""))
				orderByString += ",";
		}
		return orderByString;
	}

}

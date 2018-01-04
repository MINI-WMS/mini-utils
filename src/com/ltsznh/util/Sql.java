package com.ltsznh.util;

public class Sql {

	// public static String toSQLString(String sqlString) {
	// String sql;
	// sql = sqlString.replaceAll("select", "").replaceAll(" ", "")
	// .replace("insert", "").replace("drop", "")
	// .replace("update", "");
	//
	// return sql;
	// }

	public static String toSqlFormatText(String text) {
		return text.replaceAll("select", "select,")
				.replaceAll("delete", "delete,").replace("insert", "insert,")
				.replace("drop", "drop,").replace("update", "update,")
				.replace("alter", "alter,");
	}

	public static String[][] insertArray(String table, String[][] col,
			String[][] data) {
		String[][] notInsertData = null;
		String sql = "INSERT INTO " + table + "(";
		StringBuffer insertBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		for (int i = 1; i < data.length; i++) {
			insertBuffer.setLength(0);
			valueBuffer.setLength(0);
			// valueBuffer.delete(0, valueBuffer.length());//老方法
			insertBuffer.append(sql);
			valueBuffer.append("VALUES (");
			for (int j = 0; j < data[1].length; j++) {
				if (!data[i][j].equals("")) {
					insertBuffer.append(col[0][j] + ",");
					if (!col[1][j].equalsIgnoreCase("int")) {
						valueBuffer.append("'" + data[i][j] + "',");
					} else {
						valueBuffer.append(data[i][j] + ",");
					}
				}
			}
			int pos = insertBuffer.lastIndexOf(",");
			insertBuffer.replace(pos, pos + 1, ")");
			pos = valueBuffer.lastIndexOf(",");
			valueBuffer.replace(pos, pos + 1, ")");
			insertBuffer.append(valueBuffer);
			// Home.greetingService.exeUpdate(insertBuffer.toString(),
			// Home.userCode, new AsyncCallback<String[]>() {
			// @Override
			// public void onFailure(Throwable caught) {
			// // TODO Auto-generated method stub
			// Window.alert("系统错误，导入失败！\n" + caught.getMessage());
			// }
			// @Override
			// public void onSuccess(String[] result) {
			// // TODO Auto-generated method stub
			// // rows = rows + Integer.parseInt(result[0]);
			// if(Integer.parseInt(result[0])==0){
			// // Window.alert("插入失败!\n错误代码�? + result[1]);
			// }
			// }
			// });
		}

		return notInsertData;
	}

}

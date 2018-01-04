package com.ltsznh.db;

import com.ltsznh.model.ResultData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Bdbo {
	private   Logger logger = LogManager.getLogger(Bdbo.class);

	private Connection conn;
	private Statement stmt;
	private HashMap<String, Object> param;
	private boolean RW = false;
	

	public Bdbo(final HashMap<String, Object> param) throws Exception {
		this.param = param;
		conn = dbo.getConn();
		stmt = dbo.getStatement(conn);
	}
	
	public ResultData check() throws Exception{
		ResultData result = dbo.getUserRW(param);
		if(!result.getList().get(0)[0].equalsIgnoreCase("W")){
			result.setStatus(false);
			result.setMessage("您无增删改权限！");
			RW = false;
		}else {
			RW = true;
		}
		return result;
	}

	public void addBatch(String sqlString) throws SQLException {
		logger.info("DB", param.get("id").toString(), "addBatch:" + sqlString);
		if(RW){
			dbo.updateLog(param, sqlString, new StringBuffer("批量执行"), 0);
			stmt.addBatch(sqlString);
//			logger.info("BDB", param.get("id").toString(), sqlString);
		}
		else {
			throw new SQLException("用户无写权限！");
		}
	}

	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		logger.info("DB",param.get("id").toString(), "executeBatch...");
		int[] result = null;
		if(RW) result = stmt.executeBatch();
		long res = 0;
//		for (int i = 0; i < result.length; i++)
//			logger.info("DB", id, result[i] + "");
		for(int i = 0;i < result.length;i++){
			res += result[i];
		}
		dbo.updateLog(param, "", new StringBuffer("批量执行"), res);
		release(stmt);
		release(conn);
		return result;
	}


	/**
	 * @param conn 释放结果
	 */
	private   void release(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				logger.error("Server", "release conn", e);
			}
			conn = null;
		}
	}

	private   void release(Statement stmt) {
		// TODO Auto-generated method stub
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				logger.error("Server", "release stmt", e);
			}
			stmt = null;
		}
	}

}

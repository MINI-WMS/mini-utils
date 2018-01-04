package com.ltsznh.db;


import com.ltsznh.model.ResultData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.HashMap;

public class dbo {
	private static Logger logger = LogManager.getLogger(dbo.class);
	public static ResultData executeQuery(String sqlString, HashMap<String, Object> param) {
		return executeParamQuery(sqlString, param, null);
	}

	/**
	 * 参数化查询
	 * 
	 * @param sqlString
	 *            sql语句
	 * @param param
	 *            参数
	 * @param sqlParam
	 *            sql参数
	 * @return 返回resultData
	 * @throws SQLException
	 * @throws Exception
	 */
	public static ResultData executeParamQuery(String sqlString, HashMap<String, Object> param, String[] sqlParam) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		ResultData result = new ResultData();
		try {
			conn = getConn();
			logger.info("DB", param.get("id").toString(), sqlString);
			StringBuffer paramBuffer = new StringBuffer();
			pStmt = conn.prepareStatement(sqlString);
			if (sqlParam != null) {
				for (int i = 0; i < sqlParam.length; i++) {
					pStmt.setString(i + 1, sqlParam[i]);
					// pStmt.set

					paramBuffer.append(sqlParam[i] + ",");
				}
				logger.info("DB", param.get("id").toString(), "参数：" + paramBuffer.toString());
			}
			long sTime = System.currentTimeMillis();
			rs = pStmt.executeQuery();
			long eTime = System.currentTimeMillis();

			setResultSet(result, rs);
			result.setTime((eTime - sTime));
			logger.info("DB", param.get("id").toString(),
					"总共： " + result.size() + "行" + " 耗时： " + result.getTime() + "毫秒");
		} catch (SQLException e) {
			if (e.getSQLState().equalsIgnoreCase("08003")) {
				try {
					db.reboot();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage(
					"ERROR CODE:" + e.getSQLState() + "\nID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("ID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} finally {
			release(rs);
			release(pStmt);
			release(conn);
		}
		return result;
	}

	/**
	 * 参数化查询
	 * 
	 * @param sqlString
	 *            sql语句
	 * @param param
	 *            参数
	 * @param sqlParam
	 *            sql参数
	 * @return 返回resultData
	 * @throws SQLException
	 * @throws Exception
	 */
	public static ResultData executeParamQuerySilence(String sqlString, HashMap<String, Object> param,
			String[] sqlParam) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		ResultData result = new ResultData();
		try {
			conn = getConn();
			StringBuffer paramBuffer = new StringBuffer();
			pStmt = conn.prepareStatement(sqlString);
			if (sqlParam != null) {
				for (int i = 0; i < sqlParam.length; i++) {
					pStmt.setString(i + 1, sqlParam[i]);
					paramBuffer.append(sqlParam[i] + ",");
				}
			}
			long sTime = System.currentTimeMillis();
			rs = pStmt.executeQuery();
			long eTime = System.currentTimeMillis();

			setResultSet(result, rs);
			result.setTime((eTime - sTime));
		} catch (SQLException e) {
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage(
					"ERROR CODE:" + e.getSQLState() + "\nID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("ID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} finally {
			release(rs);
			release(pStmt);
			release(conn);
		}
		return result;
	}

	/**
	 *
	 * @param param
	 * @param sqlParam
	 * @return
	 */
	public static ResultData executeParamQueryPaging(HashMap<String, Object> param, String[] sqlParam) {
		int startRows = Integer.parseInt(param.getOrDefault("startRows", 0).toString());
		int pageSize;
		if (param.containsKey("pageSize"))
			pageSize = Integer.parseInt(param.getOrDefault("pageSize", 20).toString());
		else {
			int endRows = Integer.parseInt(param.getOrDefault("endRows", 100000).toString());
			pageSize = endRows - startRows;
		}

		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		ResultData result = new ResultData();

		String sqlString = "SELECT SQL_NO_CACHE SQL_CALC_FOUND_ROWS "
				+ param.getOrDefault("columNameString", "").toString() + " "
				+ param.getOrDefault("fromString", "").toString() + (param.containsKey("orderString")
						? " ORDER BY " + param.getOrDefault("orderString", "").toString() : "")
				+ " LIMIT " + startRows + "," + pageSize + "";

		String sqlRowsString = "SELECT FOUND_ROWS()";// 查询总行数
		try {
			conn = getConn();
			StringBuffer paramBuffer = new StringBuffer();
			logger.info("DB", param.get("id").toString(), sqlString);
			pStmt = conn.prepareStatement(sqlString);
			if (sqlParam != null) {
				for (int i = 0; i < sqlParam.length; i++) {
					pStmt.setString(i + 1, sqlParam[i]);
					// pStmt.get
					paramBuffer.append(sqlParam[i] + ",");
				}
				logger.info("DB", param.get("id").toString(), "参数：" + paramBuffer.toString());
			}
			// 查询结果
			long ssTime = System.currentTimeMillis();
			rs = pStmt.executeQuery();
			long eeTime = System.currentTimeMillis();
			setResultSetPaging(result, rs);
			// 查询总行数
			release(pStmt);
			pStmt = conn.prepareStatement(sqlRowsString);
			long sTime = System.currentTimeMillis();
			rs = pStmt.executeQuery();
			long eTime = System.currentTimeMillis();

			int rows = 0;
			if (rs.next()) {
				try {
					rows = Integer.parseInt(rs.getString(1).toString());
				} catch (Exception e) {
					e.printStackTrace();
					rows = Integer.parseInt(rs.getString(1).toString());
				}
			}
			result.setRows(rows);

			result.setTime((eeTime - ssTime) + (eTime - sTime));
			logger.info("DB", param.get("id").toString(),
					"总共： " + result.size() + "行" + " 耗时： " + (eTime - sTime) + "毫秒  分页：" + (startRows + 1) + " - "
							+ (startRows + 1 + pageSize) + " 耗时： " + (eeTime - ssTime) + "毫秒 ");
		} catch (SQLException e) {
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage(
					"ERROR CODE:" + e.getSQLState() + "\nID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("ID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} finally {
			release(rs);
			release(pStmt);
			release(conn);
		}
		return result;
	}

	/**
	 * 参数化查询
	 * 
	 * @param sqlString
	 *            sql语句
	 * @param param
	 *            参数
	 * @param sqlParam
	 *            sql参数
	 * @return 返回resultData
	 * @throws SQLException
	 * @throws Exception
	 */
	public static ResultData executeParamUpdate(String sqlString, HashMap<String, Object> param, String[] sqlParam) {
		ResultData result = getUserRW(param);
		if (!result.getStatus())
			return result;
		return executeParamUpdateN(sqlString, param, sqlParam);
	}

	/**
	 * 不验证读写权限，登录时用
	 * 
	 * @param sqlString
	 * @param param
	 * @param sqlParam
	 * @return
	 * @throws Exception
	 */
	public static ResultData executeParamUpdateN(String sqlString, HashMap<String, Object> param, String[] sqlParam) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultData result = null;
		int rs = 0;
		StringBuffer paramBuffer = new StringBuffer();
		try {
			conn = getConn();
			result = new ResultData();

			logger.info("DB", param.get("id").toString(), sqlString);

			pStmt = conn.prepareStatement(sqlString);
			if (sqlParam != null) {
				for (int i = 0; i < sqlParam.length; i++) {
					pStmt.setString(i + 1, sqlParam[i]);
					paramBuffer.append(sqlParam[i] + ",");
				}
				logger.info("DB", param.get("id").toString(), "参数：" + paramBuffer.toString());
			}
			long sTime = System.currentTimeMillis();
			rs = pStmt.executeUpdate();
			long eTime = System.currentTimeMillis();

			result.setRows(rs);
			logger.info("DB", param.get("id").toString(),
					"共更新： " + result.getRows() + "行" + " 耗时： " + (eTime - sTime) + "毫秒");
		} catch (SQLException e) {
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage(
					"ERROR CODE:" + e.getSQLState() + "\nID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("ID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} finally {
			release(pStmt);
			release(conn);
			updateLog(param, sqlString, paramBuffer, rs);
		}
		return result;
	}

	/**
	 * 参数化查询
	 * 
	 * @param sqlString
	 *            sql语句
	 * @param param
	 *            参数
	 * @param sqlParam
	 *            sql参数
	 * @return 返回resultData
	 * @throws SQLException
	 * @throws Exception
	 */
	public static ResultData executeParamUpdateNSilence(String sqlString, HashMap<String, Object> param,
			String[] sqlParam) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		int rs = 0;
		ResultData result = new ResultData();
		try {
			conn = getConn();
			StringBuffer paramBuffer = new StringBuffer();

			pStmt = conn.prepareStatement(sqlString);
			if (sqlParam != null) {
				for (int i = 0; i < sqlParam.length; i++) {
					pStmt.setString(i + 1, sqlParam[i]);
					paramBuffer.append(sqlParam[i] + ",");
				}
			}
			long sTime = System.currentTimeMillis();
			rs = pStmt.executeUpdate();
			long eTime = System.currentTimeMillis();

			result.setRows(rs);
			result.setTime((eTime - sTime));
		} catch (SQLException e) {
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage(
					"ERROR CODE:" + e.getSQLState() + "\nID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(param.get("id").toString(), e);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("ID:" + param.get("id").toString() + "\n系统异常，请联系管理员！");
		} finally {
			// release(rs);
			release(pStmt);
			release(conn);
		}
		return result;
	}

	public static ResultData getUserRW(HashMap<String, Object> param) {
		ResultData result;
		try {
			logger.info("DB", param.get("id").toString(), "获取用户读写权限");
			// String sqlString = "SELECT rw FROM sys_users WITH(NOLOCK) WHERE
			// (userCode = ?) AND (rw = 'W') ";//sql server
			String sqlString = "SELECT 1 FROM sys_users WHERE (userId = ?) AND (rw = 'W') ";// mysql
			result = executeParamQuerySilence(sqlString, param, new String[] { param.get("userId").toString() });
			if (!result.getStatus() || result.size() < 1) {
				result.setStatus(false);
				result.setMessage("您无增删改权限！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			result = new ResultData();
			result.setMessage("ID:" + param.get("id").toString());
			result.setStatus(false);
			logger.error("getUserRW", param.get("id").toString(), e);
		}
		return result;
	}

	/**
	 * 将rs赋值到resultData
	 * 
	 * @param rd
	 * @param rs
	 * @throws SQLException
	 */
	public static void setResultSetPaging(ResultData rd, ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		ResultSetMetaData mdData;
		try {
			mdData = rs.getMetaData();
			int cols = mdData.getColumnCount();// 获取列数
			// 赋值列名
			String[] colsName = new String[cols];
			for (int j = 0; j < cols; j++) {
				colsName[j] = mdData.getColumnLabel(j + 1);
			}
			rd.setColNames(colsName);

			// 赋值查询结果
			while (rs.next()) {
				String[] Objects = new String[cols];
				for (int j = 0; j < cols; j++) {
					Objects[j] = rs.getString(j + 1);
				}
				rd.add(Objects);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw e;
		} finally {
			mdData = null;
		}
	}

	/**
	 * 将rs赋值到resultData
	 * 
	 * @param rd
	 * @param rs
	 * @throws SQLException
	 */
	public static void setResultSet(ResultData rd, ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		ResultSetMetaData mdData;
		try {
			mdData = rs.getMetaData();
			int cols = mdData.getColumnCount();// 获取列数
			// 赋值列名
			String[] colsName = new String[cols];
			for (int j = 0; j < cols; j++) {
				colsName[j] = mdData.getColumnLabel(j + 1);
			}
			rd.setColNames(colsName);

			// 赋值查询结果
			while (rs.next()) {
				String[] Objects = new String[cols];
				for (int j = 0; j < cols; j++) {
					try {
						if (rs.getBytes(j + 1) != null)
							Objects[j] = new String(rs.getBytes(j + 1), "utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Objects[j] = rs.getString(j + 1);
						// new String(rs.getBytes(j + 1), PARAM.ENCODING);
					}
				}
				rd.add(Objects);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw e;
		} finally {
			mdData = null;
		}
	}

	public static void updateLog(HashMap<String, Object> param, String sqlString, StringBuffer paramBuffer, long rows) {
		// HttpServletRequest request = (HttpServletRequest)
		// param.get("request");
		try {
			String sqlLog;
			String[] sqlLogParam;

			sqlLog = "INSERT INTO sys_log(userId, loginId, ip, pip, lType, lStatus, sqlString, sqlParam, lMessage, userAgent) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
			sqlLogParam = new String[] { param.getOrDefault("userId", "").toString(),
					param.getOrDefault("id", "").toString(), param.getOrDefault("ip", "").toString(),
					param.getOrDefault("iip", "").toString(), param.getOrDefault("type", "").toString(), "" + rows,
					sqlString.toString(), paramBuffer.toString(), param.getOrDefault("message", "").toString(),
					param.getOrDefault("user-agent", "").toString() };

			executeParamUpdateNSilence(sqlLog, param, sqlLogParam);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateLog", param.get("id").toString(), e);
		}

	}

	/**
	 * 获取连接
	 * 
	 * @return statement
	 */
	public synchronized static Connection getConn() throws Exception {
		Connection conn = null;
		try {
			// conn = db.getBasicDataSource().getConnection();
			conn = db.getPool().getConnection();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("DB", "", e.getLocalizedMessage());
		} finally {
			if (conn == null || conn.isClosed()) {
				conn = db.getPool().getConnection();
			}

		}
		return conn;
	}

	/**
	 * 获取连接状态
	 * 
	 * @return statement
	 */
	public synchronized static Statement getStatement(Connection conn) throws Exception {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (stmt == null) {// 创建链接状态
				logger.info("Server", "getStatement", "stmt为空，生成状态！");
				try {
					// stmt =
					// conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);//sqlserver
					stmt = conn.createStatement();// sqlite
				} catch (Exception e) {
					try {
						logger.error("Server", "getStatement", e);
						logger.info("Server", "getStatement", "创建链接失败，重新生成连接...");
						stmt = conn.createStatement();
					} catch (Exception ee) {
						logger.error("Server", "getStatement", ee);
					}
				}
			}
		}
		return stmt;
	}

	/**
	 *
	 * @param rs
	 */
	private static void release(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				logger.error("Server", "release rs", e);
			}
			rs = null;
		}
	}

	/**
	 * @param conn 释放结果
	 */
	private static void release(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				logger.error("Server", "release conn", e);
			}
			conn = null;
		}
	}

	private static void release(PreparedStatement pStmt) {
		// TODO Auto-generated method stub
		if (pStmt != null) {
			try {
				pStmt.close();
			} catch (Exception e) {
				logger.error("Server", "release stmt", e);
			}
			pStmt = null;
		}
	}
}

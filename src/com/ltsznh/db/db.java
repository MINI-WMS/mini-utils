package com.ltsznh.db;

import com.ltsznh.util.Encryption;
import com.ltsznh.util.XML;
import com.ltsznh.util.parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.sql.SQLException;

//数据源配置

public class db {
	private static Logger logger = LogManager.getLogger(db.class);

	// public static void main(String[] args){
	// new DataSource();
	// new db().getPool();
	// }
	private static DataSource ds;

	private static ConnectionPool connPool;

	static {
		getPool();
	}

	public static ConnectionPool getPool() {
		if (connPool == null || connPool.isClosed()) {
			try {
				connPool = getBasicDataSource().createPool();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Server", "CreatePool", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Server", "CreatePool", e);
			}
		}

		return connPool;
	}

	private static DataSource getBasicDataSource() throws Exception {
		new DataSource();
		if (ds == null || ds.getPool() == null || ds.getPool().isClosed()) {// 检查是否
			initDataSource();
		}
		return ds;
	}

	private static void initDataSource() throws Exception {
		logger.info("Server", "getBasicDataSource", "获取数据源......");
		XML xr = new XML();
		byte[] b = xr.readXmlFile(parameters.CONFIG_PATH_STRING);
		byte[] xmlByte = Encryption.DecryptByAES(new String(b), parameters.ENCRYPTION_STRING);
		// xr.setDocument(b);
		xr.setDocument(xmlByte);

		String driver = xr.getItem("jdbc", "driver");
		String url = xr.getItem("jdbc", "url");
		String user = xr.getItem("jdbc", "user");
		String pwd = xr.getItem("jdbc", "password");

		int max = 100;
		int init = 10;
		int idle = 10;
		try {
			max = Integer.parseInt(xr.getItemOrDefault("jdbc", "pool", "MaxActive", "0"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			init = Integer.parseInt(xr.getItemOrDefault("jdbc", "pool", "init", "0"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			idle = Integer.parseInt(xr.getItemOrDefault("jdbc", "pool", "MaxIdle", "0"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		xr.close();
		xr = null;

		ds = null;
		// long sTime = System.currentTimeMillis();
		// logger.info("DB", "", "url:" + url + "user:" + user + "pwd:" +
		// pwd);

		// Java.util.Properties prop = new java.util.Properties();
		// prop.put( " charSet " , " Big5 " );
		// prop.put( " user " , username);
		// prop.put( " password " , password);
		// PoolProperties poolProperties = new PoolProperties();
		ds = new DataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(user);
		ds.setPassword(pwd);
		ds.setUrl(url + "?useUnicode=true&characterEncoding=utf8");
		
		ds.getPoolProperties().setTestOnBorrow(true);

		// ds.set

		if (max > 0)
			ds.setMaxActive(max);
		if (idle > 0)
			ds.setMaxIdle(idle);
		if (init > 0)
			ds.setInitialSize(init);

		// long eTime = System.currentTimeMillis();
		// DBLog.log("Init DataSource", " 耗时： " + (eTime - sTime) + "毫秒");
		logger.info("Server", "DataSource Info:",
				new StringBuffer(
						"最大连接数:" + ds.getMaxActive() + " 最大空闲数：" + ds.getMaxIdle() + " 初始化数量：" + ds.getInitialSize())
								.toString());
		// ds.setMaxTotal(100);
	}

	public static void reboot() throws Exception {
		connPool = null;
		ds.close();
		ds = null;
		try {
			getBasicDataSource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("DB init", "", e);
			throw e;
		}
	}

	public static Object getDsMessage() {
		StringBuffer message = new StringBuffer();
		// TODO Auto-generated method stub
		message.append("===数据源信息===");
		message.append("\n");
		message.append("连接池最大连接数:");
		message.append(ds.getMaxActive());
		message.append("\n");
		message.append("连接池最大空闲数:");
		message.append(ds.getMaxIdle());
		message.append("\n");
		message.append("连接池最长等待时间:");
		message.append(ds.getMaxWait());
		message.append("\n");
		message.append("\n");
		message.append("最长时间:");
		message.append(ds.getMaxAge());
		message.append("\n");
		message.append("连接池最小空闲数:");
		message.append(ds.getMinIdle());
		message.append("\n");
		message.append("连接池初始化连接数:");
		message.append(ds.getInitialSize());
		message.append("\n");
		message.append("\n");
		message.append("活动连接数:");
		message.append(ds.getActive());
		message.append("\n");
		message.append("空闲连接数:");
		message.append(ds.getIdle());
		message.append("\n");
		message.append("合计连接数:");
		message.append(ds.getPoolSize());
		message.append("\n");
		message.append("\n");

		message.append("是否有锁:");
		message.append(ds.getUseLock());
		message.append("\n");

		message.append("连接等待数:");
		message.append(ds.getWaitCount());
		message.append("\n");

		message.append("连接数:");
		message.append(ds.getSize());
		message.append("\n");
		message.append("getNumActive:");
		message.append(ds.getNumActive());
		message.append("\n");
		message.append("getNumIdle:");
		message.append(ds.getNumIdle());
		message.append("\n");
		message.append("\n");
		return message.toString();
	}

}
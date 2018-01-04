package com.ltsznh.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;


public class parameters {
	private static Logger logger = LogManager.getLogger(parameters.class);

	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HHmmss SSS");
	public final static SimpleDateFormat SIMPLEDA_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	public static String appName = "";
	public static boolean isServer = false;

	public static boolean isUpdate = false;

	public static String WEB_SERVER_PATH = System.getProperty("user.dir");
	public static String dataFilePath;
	public static String CONFIG_PATH_STRING;// tomcat环境根目录下，防止更新影响配置文件

	public static String ENCRYPTION_STRING = "kdfalsdjfalsjf";// tomcat环境根目录下，防止更新影响配置文件
	// public static String ENCRYPTION_STRING = "kdfalsdjfalsjf";//
	// tomcat环境根目录下，防止更新影响配置文件

	// public final static Timer timer = new Timer();

	// 初始化变量
	static {
		isServer = new IsServer().getServer();
		logger.info("server", "", WEB_SERVER_PATH);

		if (WEB_SERVER_PATH == null || WEB_SERVER_PATH.trim().replace(File.separator, "").equals(""))
			WEB_SERVER_PATH = "/usr/local/tomcat9/bin";
		dataFilePath = WEB_SERVER_PATH + File.separator + "webappsData";
		CONFIG_PATH_STRING = WEB_SERVER_PATH +
		// System.getProperty("user.dir") +
				File.separator + "WEB-INF" + File.separator + "config" + File.separator + getConfigXML();

		// Thread thread = new Thread(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// Calendar calendar = Calendar.getInstance(TimeZone
		// .getTimeZone("GMT+8"));
		// calendar.set(Calendar.HOUR_OF_DAY, 1);
		// timer.schedule(autoUpdateTask, calendar.getTime(),
		// 30 * 60 * 1000L);
		// }
		// });
		// thread.start();

	}

	// public static String getRootPath(HttpServletRequest request) {
	// if (rootPath.equals("")) {
	// rootPath = request.getSession().getServletContext().getRealPath("");
	// moveFile();
	// }
	// return rootPath;
	// }

	// 获取配置
	protected static String getParam(String code, String code2) {
		XML xr = new XML();
		byte[] b;
		try {
			logger.info("server", "", CONFIG_PATH_STRING);
			b = xr.readXmlFile(CONFIG_PATH_STRING);
			byte[] xmlByte = Encryption.DecryptByAES(new String(b), ENCRYPTION_STRING);
			xr.setDocument(xmlByte);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String item = xr.getItem(code, code2);
		xr.close();
		return item;
	}

	private static String getConfigXML() {
		// TODO Auto-generated method stub
		if (!appName.equals("")) {
			return getConfigName();
		}
		String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		File file = new File(classPath);
		file = file.getParentFile().getParentFile();
		appName = file.getName();
		return getConfigName();
	}

	private static String getConfigName() {
		// TODO Auto-generated method stub
		if (appName.equalsIgnoreCase("root")) {
			return "config.xml";
		} else {
			return appName + "_config.xml";
		}
	}

	// private static void moveFile() {
	// Thread thread = new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (!rootPath.equals("") && !appName.equals("")) {
	// CopyFile copyFile = new CopyFile();
	// copyFile.copyFolder(rootPath + File.separator + "file",
	// dataFilePath + File.separator + appName
	// + File.separator + "file");
	//
	// copyFile.copyFolder(rootPath + File.separator + "WEB-INF"
	// + File.separator + "template", dataFilePath
	// + File.separator + appName + File.separator
	// + "WEB-INF" + File.separator + "template");
	//
	// copyFile.copyFolder(rootPath + File.separator + "WEB-INF"
	// + File.separator + "lib", "lib");
	// }
	// }
	// });
	// thread.start();
	// }

	// public static TimerTask autoUpdateTask = new TimerTask() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// logger.info("Server", "自动更新", "启动自动更新...");
	// new AutoUpdate().checkAndUpdate();
	// }
	// };

}

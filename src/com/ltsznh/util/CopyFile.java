package com.ltsznh.util;

import com.ltsznh.db.Bdbo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class CopyFile {
	private static Logger logger = LogManager.getLogger(CopyFile.class);
	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void copyFolder(File src, File dest) {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdirs();
				logger.info("Server", this.getClass().toString(),
						"\nCOPY Directory:" + dest.getAbsolutePath());
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				if (!destFile.exists() || destFile.isDirectory())
					// 递归复制
					copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in;
			try {
				in = new FileInputStream(src);
				OutputStream out = new FileOutputStream(dest);

				byte[] buffer = new byte[1024];

				int length;
				logger.info(
						"Server",
						this.getClass().toString(),
						"\nCOPY File:" + src.getAbsolutePath() + "\nTO:"
								+ dest.getAbsolutePath());
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				logger.info("Server", this.getClass().toString(),
						e.getLocalizedMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("Server", this.getClass().toString(),
						e.getLocalizedMessage());
			}
		}
	}

	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void copyFolder(String src, String dest) {
		File srcFile = new File(src);
		File destFile = new File(dest);
		copyFolder(srcFile, destFile);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CopyFile().copyFolder("", "");
	}

}

package com.ltsznh.util;

import com.ltsznh.db.Bdbo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by 涛 on 2015/07/28
 */
public class TemplateFile {
	private static Logger logger = LogManager.getLogger(TemplateFile.class);

	public HSSFWorkbook getExcel(String filePath) {
		File file = new File(parameters.dataFilePath + File.separator
				+ parameters.appName + File.separator + "template"
				+ File.separator + "excel" + File.separator + filePath);
		logger.info("Server", "get excel template", file.getAbsolutePath());
		// file.setWritable(true);
		try {
			return new HSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getHtml(String filePath) {
		File file = new File(parameters.dataFilePath + File.separator
				+ parameters.appName + File.separator + "template"
				+ File.separator + "html" + File.separator + filePath);
		logger.info("Server", "get html template", file.getAbsolutePath());
		// file.setWritable(true);

		return new getHtmlTemplate().getHtml(file);

	}
}

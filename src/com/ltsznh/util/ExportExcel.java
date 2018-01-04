package com.ltsznh.util;


import com.ltsznh.db.db;
import com.ltsznh.model.ResultData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 涛 on 2015/09/19. 导出Excel
 */
public class ExportExcel {
	private static Logger logger = LogManager.getLogger(ExportExcel.class);
	public static void Export(HashMap<String, Object> param, ResultData result,
			String fileName) {
		// HttpServletResponse response = (HttpServletResponse) param
		// .get("response");

		// HttpServletRequest request = (HttpServletRequest)
		// param.get("request");

		Workbook workbook = null;
		Sheet sheet;
		Row row;
		CellStyle cellStyle;
		if (fileName.endsWith(".xls")) {
			fileName = fileName.replace(".xls", "")
					+ Tools.sdf.format(new Date())
					+ (int) ((Math.random() + 1) * 100) + ".xls";
			// workbook = new HSSFWorkbook();//创建新的Excel 工作簿
			// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		} else {
			fileName = fileName.replace(".xlsx", "")
					+ Tools.sdf.format(new Date())
					+ (int) ((Math.random() + 1) * 100) + ".xlsx";
			workbook = new XSSFWorkbook();
			// cellStyle.setDataFormat(XSSFDataFormat.getBuiltinFormat("@"));
		}

		sheet = workbook.createSheet("Sheet1");// 在Excel 工作簿中建一工作表
		sheet.createFreezePane(1, 2);
		cellStyle = workbook.createCellStyle();// 设置单元格格式(文本)
//		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
//		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
//		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setWrapText(true);

		Font cellFont = workbook.createFont();
		cellFont.setFontHeightInPoints((short) 10);

		cellStyle.setFont(cellFont);

		long rows = result.size();
		int columns = result.getCols();
		String str;
		// 将值写入excel
		for (int i = 0; i < rows; i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < columns; j++) {
				Cell cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);// 设置单元格格式
				str = (result.getList().get(i)[j]);
				cell.setCellValue(str);// 赋值
				if (str != null) {
					try {// 自适应中英文列宽
						int with;
						if (str.getBytes("GBK").length > 255) {
							with = 255 * 256;
						} else {
							with = str.getBytes("GBK").length * 256;
						}
						if (with > sheet.getColumnWidth(j))
							sheet.setColumnWidth(j, with);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		// 写标题、设置自动列宽
		Font titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 11);
//		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle titleCellStyle = workbook.createCellStyle();
//		titleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//		titleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//		titleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
//		titleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
		titleCellStyle.setFont(titleFont);
		row = sheet.createRow(0);
		for (int j = 0; j < columns; j++) {
			Cell cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(titleCellStyle);// 设置单元格格式
			str = (result.getColNames()[j]);
			cell.setCellValue(str);// 赋值
			// sheet.autoSizeColumn(j);//设置自动列宽
			// sheet.setColumnWidth(j, (int)(sheet.getColumnWidth(j) * 1.5));
			if (str != null) {
				try {// 自适应中英文列宽
					int with = str.getBytes("GBK").length * 256;
					if (with > sheet.getColumnWidth(j))
						sheet.setColumnWidth(j, with);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		logger.info("Server", param.getOrDefault("id", "").toString(),
				fileName);

		result.setMessage(fileName);// 返回文件名

		File path = null;
		String filePath = null;
		try {
			filePath = parameters.dataFilePath + File.separator
					+ parameters.appName + File.separator + "file"
					+ File.separator + "excel" + File.separator + fileName;

			path = new File(filePath);// 删除路径下同名的Excel 文件
			File ppath = path.getParentFile();
			if (!ppath.exists()) {
				ppath.mkdirs();
			}
			if (path.exists())
				path.delete();

			FileOutputStream fOut = null;// 新建一输出文件流
			fOut = new FileOutputStream(filePath);

			workbook.write(fOut);// 把相应的Excel 工作簿存盘
			fOut.flush();// 操作结束，关闭文件
			fOut.close();
			fOut = null;
			workbook = null;

			// FileChannel fc = null;
			// @SuppressWarnings("resource")
			// RandomAccessFile randomAccessFile = new
			// RandomAccessFile(filePath,
			// "r");
			// fc = randomAccessFile.getChannel();
			// MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0L,
			// fc.size()).load();
			//
			// byte[] bos = new byte[(int) fc.size()];
			// if (byteBuffer.remaining() > 0) {
			// byteBuffer.get(bos, 0, byteBuffer.remaining());
			// }
			//
			// // 解决下载文件名中文乱码问题
			// response.setHeader("Content-Disposition",
			// "attachment; filename=\""
			// + URLEncoder.encode(fileName, PARAM.ENCODING) + "\"");
			//
			// // response.setHeader("Content-Disposition",
			// "attachment; filename="
			// // +
			// // java.net.URLEncoder.encode(fileName, "UTF-8"));
			//
			// response.setContentLength(bos.length);
			// // response.setCharacterEncoding(encoding);
			// response.getOutputStream().write(bos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// path = new File(filePath);// 删除路径下同名的Excel 文件
			// if (path.exists())
			// path.delete();
		}

	}

	public static void mergeCellCol(Workbook workbook, Sheet sheet,
			int mergeCol, int rows, int lastIndex) {
		CellStyle cellStyle;
		cellStyle = workbook.createCellStyle();// 设置单元格格式(文本)
//		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
//		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
//		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setWrapText(true);

		String lastString;
		lastString = sheet.getRow(lastIndex).getCell(mergeCol)
				.getStringCellValue();
		// System.out.println(lastString);
		sheet.getRow(lastIndex).getCell(0).setCellStyle(cellStyle);
		for (int i = lastIndex + 1; i < rows; i++) {
			if (sheet.getRow(i).getCell(mergeCol).getStringCellValue()
					.equals(lastString)
					&& (mergeCol < 1 || sheet
							.getRow(i)
							.getCell(mergeCol - 1)
							.getStringCellValue()
							.equals(sheet.getRow(i - 1).getCell(mergeCol - 1)
									.getStringCellValue()))) {
				continue;
			} else {
				sheet.addMergedRegion(new CellRangeAddress(lastIndex, i - 1,
						mergeCol, mergeCol));

				lastIndex = i;
				lastString = sheet.getRow(lastIndex).getCell(mergeCol)
						.getStringCellValue();
				sheet.getRow(lastIndex).getCell(mergeCol)
						.setCellStyle(cellStyle);
				continue;
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(lastIndex, rows, mergeCol,
				mergeCol));
	}
}

package com.ltsznh.util;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * 利用POI读取excel,转换为html
 *
 */
public class ExcelToHtml {
	public static void main(String[] args) {
		new ExcelToHtml()
				.getHtml("war\\WEB-INF\\template\\excel\\btkrqrjbb.xls");
	}

	public String getHtml(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return getHtml(file);
		else
			return "";
	}

	public String getHtml(File file) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fis == null) {
			return "";
		} else {
			return getHtml(fis);
		}

	}

	public String getHtml(FileInputStream fis) {
		// TODO Auto-generated method stub
		HSSFWorkbook excelBook;

		try {
			excelBook = new HSSFWorkbook(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		System.out.println("test");
		return getHtml(excelBook);
	}

	public String getHtml(HSSFWorkbook excelBook) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outStream = null;
		try {
			ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.newDocument());
			// 设置输出格式
			excelToHtmlConverter.setOutputColumnHeaders(false);// 列名
			excelToHtmlConverter.setOutputRowNumbers(false);// 行号
			excelToHtmlConverter.setOutputLeadingSpacesAsNonBreaking(true);
			// 处理excel表格
			excelToHtmlConverter.processWorkbook(excelBook);
//			excelToHtmlConverter.proce

			// List pics = excelBook.getAllPictures();
			// if (pics != null) {
			// for (int i = 0; i < pics.size(); i++) {
			// Picture pic = (Picture) pics.get(i);
			// try {
			// pic.writeImageContent(new FileOutputStream(path
			// + pic.suggestFullFileName()));
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// }
			// }
			// }
			Document htmlDocument = excelToHtmlConverter.getDocument();
			outStream = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(outStream);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			// serializer.setOutputProperty(OutputKeys.ENCODING,
			// PARAM.ENCODING);
			serializer.setOutputProperty(OutputKeys.ENCODING, "GBK");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
			outStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String content = new String(outStream.toByteArray());

		// FileUtils.write(new File(path, "excel.html"), content, "gb2312");
		// System.out.println(content);

		return content;
	}

}

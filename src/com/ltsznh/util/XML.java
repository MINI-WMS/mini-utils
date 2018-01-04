package com.ltsznh.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class XML {
	private String filePath;
	private Element root;
	private Document doc;

	public void loadXmlFile(String filePath) throws Exception {
		this.filePath = filePath;
		File file = new File(filePath);
		loadXmlFile(file);
	}

	public void loadXmlFile(File file) throws Exception {
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) throw new Exception("xml文件不存在，创建目录失败！");
		}
		filePath = file.getPath();
		SAXReader reader = new SAXReader();
		doc = reader.read(file);
		root = doc.getRootElement();
	}

	public void loadXmlFile(byte[] file) throws DocumentException, IOException {
		InputStream in = new ByteArrayInputStream(file);
		SAXReader reader = new SAXReader();
		doc = reader.read(in);
		root = doc.getRootElement();
	}

	// public byte[] readEncryptionXmlFile(String filePath) throws Exception {
	// this.filePath = filePath;
	// @SuppressWarnings("resource")
	// FileInputStream is = new FileInputStream(filePath);
	// byte[] b = new byte[(int) is.getChannel().size()];
	// is.read(b);
	// return b;
	// }

	public byte[] readXmlFile(String filePath) throws Exception {
		this.filePath = filePath;
		@SuppressWarnings("resource")
		FileInputStream is = new FileInputStream(filePath);
		byte[] b = new byte[(int) is.getChannel().size()];
		is.read(b);
		return b;
	}

//	public byte[] readEncryptionHexXmlFile(String filePath) throws Exception {
//		this.filePath = filePath;
//		@SuppressWarnings("resource")
//		FileInputStream is = new FileInputStream(filePath);
//		byte[] b = new byte[(int) is.getChannel().size()];
//		is.read(b);
//		StringBuffer sBuffer = new StringBuffer();
//		for (int i = 0; i < b.length; i++)
//			sBuffer.append((char) b[i]);
//
//		return Encryption.hex2Bytes(sBuffer.toString());
//	}

	public void setDocument(byte[] xmlByte) throws IOException, DocumentException {
		loadXmlFile(xmlByte);
	}

	public Document getDocument() {
		return doc;
	}

	public String getItem(String code) {
		String item = root.elementText(code);
		if (item == null) {
			root.addElement(code);
			return root.elementText(code);
		}
		return item;
	}

	public String getItem(String code, String code2) {
		String item = getElement(code).elementText(code2);
		if (item == null) {
			getElement(code).addElement(code2);
			return getElement(code).elementText(code2);
		}
		return item;
	}

	public String getItem(String code, String code2, String code3) {
		String item = getElement(getElement(code), code2).elementText(code3);
		if (item == null) {
			getElement(getElement(code), code2).addElement(code3);
			return getElement(getElement(code), code2).elementText(code3);
		}
		return item;
	}

	public String getItemOrDefault(String code, String value) {
		String itemString = root.elementText(code);
		if (itemString == null) {
			root.addElement(code);
			root.element(code).setText(value);
			return value;
		}
		return itemString;
	}

	public String getItemOrDefault(String code, String code2, String value) {
		String itemString = getElement(code).elementText(code2);
		if (itemString == null) {
			getElement(code).addElement(code2);
			getElement(code).element(code2).setText(value);
			;
			return value;
		}
		return itemString;
	}

	public String getItemOrDefault(String code, String code2, String code3, String value) {
		String itemString = getElement(getElement(code), code2).elementText(code3);
		if (itemString == null) {
			getElement(getElement(code), code2).addElement(code3);
			getElement(getElement(code), code2).element(code3).setText(value);
			return value;
		}
		return itemString;
	}

	public Element getElement(String code) {
		Element ele = root.element(code);
		if (ele == null) {
			root.addElement(code);
			return root.element(code);
		}
		return ele;
	}

	public Element getElement(Element ele, String code) {
		Element element = ele.element(code);
		if (element == null) {
			ele.addElement(code);
			return ele.element(code);
		}
		return element;
	}

	public void close() {
		// TODO Auto-generated method stub
		root.clearContent();
		root = null;
		doc.clearContent();
		doc = null;
		filePath = null;
	}

	public void saveXmlFile() throws IOException {
		// TODO Auto-generated method stub
		saveXmlFile(doc);
	}

	public void saveXmlFile(Document document) throws IOException {
		// TODO Auto-generated method stub
		XMLWriter writer;
		OutputFormat format = OutputFormat.createPrettyPrint();
		writer = new XMLWriter(new FileWriter(filePath), format);
		writer.write(document);
		writer.close();
	}

	public void saveXmlFile(byte[] document) throws IOException {
		// TODO Auto-generated method stub
		OutputStream o = new FileOutputStream(filePath);
		o.write(document);
		o.flush();
		o.close();
	}
}

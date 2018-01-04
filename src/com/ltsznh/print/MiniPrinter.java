package com.ltsznh.print;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.*;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MiniPrinter {
	private Logger logger = LogManager.getLogger(this.getClass());

	private String printerName = null;
	private PrintService printer = null;
	private PrintRequestAttributeSet aset = null;

	private DocFlavor psInFormat = null;//设置打印数据的格式，此处为图片gif格式

	/**
	 * 初始化要打印的打印机
	 */
	private void initPrinter() {
		if (null == printerName) return;

		aset = new HashPrintRequestAttributeSet();// 设置打印属性
//		aset.add(new Copies(3));//打印份数，3份

		PrintService services[] = PrintServiceLookup.lookupPrintServices(psInFormat, aset);//查找所有打印服务

		// this step is necessary because I have several printers configured
		//将所有查找出来的打印机与自己想要的打印机进行匹配，找出自己想要的打印机

		for (int i = 0; i < services.length; i++) {
			logger.info("打印机: " + services[i].getName());
			if (services[i].getName().contains(printerName)) {
				printer = services[i];
				logger.info("获取到打印机: " + services[i].getName());
				logger.info("打印机初始化成功: " + printer);
				break;
			}
		}
	}

	/**
	 * 开始打印内容
	 *
	 * @return 返回打印是否成功
	 */
	public boolean print() {
		if (null == printer) {
			logger.error("未找到打印机：" + printerName);
			return false;
		}
		FileInputStream psStream = null;
		try {
			psStream = new FileInputStream("D:\\1.jpg");
		} catch (FileNotFoundException ffne) {
			ffne.printStackTrace();
		}
		if (psStream == null) {
			return false;
		}

		//创建打印数据
//      DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性
//      Doc myDoc = new SimpleDoc(psStream, psInFormat, docAttr);
		Doc myDoc = new SimpleDoc(psStream, psInFormat, null);


		//可以输出打印机的各项属性
		AttributeSet att = printer.getAttributes();

		for (Attribute a : att.toArray()) {

			String attributeName;
			String attributeValue;

			attributeName = a.getName();
			attributeValue = att.get(a.getClass()).toString();

			System.out.println(attributeName + " : " + attributeValue);
		}

		if (printer != null) {
			DocPrintJob job = printer.createPrintJob();//创建文档打印作业
			try {
				job.print(myDoc, aset);//打印文档

			} catch (Exception pe) {
				pe.printStackTrace();
			}
		} else {
			logger.error("未找到打印机：" + printerName.toString());
		}

		return true;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public DocFlavor getPsInFormat() {
		return psInFormat;
	}

	public void setPsInFormat(DocFlavor psInFormat) {
		this.psInFormat = psInFormat;
	}


	public static void main(String args[]) {
		MiniPrinter printer = new MiniPrinter();
		printer.setPrinterName("XPS");// 设置要打印输出的打印机
		printer.setPsInFormat(DocFlavor.INPUT_STREAM.GIF);// 设置格式
		printer.initPrinter();
		printer.print();

	}
}
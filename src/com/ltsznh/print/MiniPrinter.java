package com.ltsznh.print;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.*;
import javax.print.attribute.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 打印机，可打印票据、文档、图片等
 */
public class MiniPrinter {
	private Logger logger = LogManager.getLogger(this.getClass());

	private String printerName = null;// 打印机名称
	private PrintService printer = null;// 打印机服务
	private PrintRequestAttributeSet aset = null;// 打印属性
	private DocFlavor psInFormat = null;//设置打印数据的格式
	private Doc printDoc = null;

	/**
	 * 初始化要打印的打印机
	 */
	private boolean initPrinter() {
		if (null == printerName) return false;
		if (null == psInFormat) return false;
		if (null != printer) return true;
		aset = new HashPrintRequestAttributeSet();
//		PrintService services[] = PrintServiceLookup.lookupPrintServices(psInFormat, aset);//查找所有打印服务
		PrintService services[] = PrintServiceLookup.lookupPrintServices(null, null);//查找所有打印服务
//		PrintService services = PrintServiceLookup.lookupDefaultPrintService();//查找所有打印服务

		logger.info("获取到" + services.length + "个打印机，开始匹配打印机：" + printerName);
		// 将所有打印机与设置打印机名称进行匹配。分三种方式匹配：精确、忽略大小写、模糊包含
		// 精确匹配打印机
		for (int i = 0; i < services.length; i++) {
			logger.info("匹配打印机: " + services[i].getName());
			if (services[i].getName().equals(printerName)) {
				printer = services[i];
				logger.info("获取到打印机: " + services[i].getName());
				logger.info("打印机初始化成功: " + printer);
				break;
			}
		}
		// 忽略大小写匹配打印机
		if (printer == null) {
			for (int i = 0; i < services.length; i++) {
				if (services[i].getName().equalsIgnoreCase(printerName)) {
					printer = services[i];
					logger.info("获取到打印机: " + services[i].getName());
					logger.info("打印机初始化成功: " + printer);
					break;
				}
			}
		}
		// 包含名称匹配打印机
		if (printer == null) {
			for (int i = 0; i < services.length; i++) {
				if (services[i].getName().contains(printerName)) {
					printer = services[i];
					logger.info("获取到打印机: " + services[i].getName());
					logger.info("打印机初始化成功: " + printer);
					break;
				}
			}
		}
		if (printer != null) {// 如果找到打印机，输出打印机各项属性
			//可以输出打印机的各项属性
			AttributeSet att = printer.getAttributes();
			for (Attribute a : att.toArray()) {
				logger.info(a.getName() + " : " + att.get(a.getClass()).toString());
			}
		}
		return (null != printer);// 返回是否匹配到打印机
	}

	public String getPrinterName() {
		return printerName;
	}

	/**
	 * 设置打印机名称
	 *
	 * @param printerName
	 */
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public DocFlavor getPsInFormat() {
		return psInFormat;
	}

	/**
	 * 设置打印文档格式
	 *
	 * @param psInFormat
	 */
	public void setPsInFormat(DocFlavor psInFormat) {
		if (null == printer)
			this.psInFormat = psInFormat;
		else {
			if (printer.isDocFlavorSupported(psInFormat))// 如果打印机支持该格式，则赋值
				this.psInFormat = psInFormat;
		}
	}

	public void setPrintDoc(Doc printDoc) {
		this.printDoc = printDoc;
	}

	public void setPrintDoc(String filePath) throws Exception {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw e;
		}
		if (fis == null) {
			logger.error("设置打印内容失败:" + filePath);
			throw new Exception("设置打印内容失败:" + filePath);
		}
		//创建打印数据
		DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性
		Doc doc = new SimpleDoc(fis, psInFormat, docAttr);
//		Doc doc = new SimpleDoc(fis, psInFormat, null);

		setPrintDoc(doc);
	}

	public void setPrintDocString(String printDoc) throws Exception {
		//创建打印数据
		DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性
//		Doc doc = new SimpleDoc(fis, psInFormat, docAttr);
		Doc doc = new SimpleDoc(printDoc, psInFormat, null);
//		Doc doc = new SimpleDoc(fis, psInFormat, null);

		setPrintDoc(doc);
	}

	/**
	 * 开始打印内容
	 *
	 * @return 返回打印是否成功
	 */
	public boolean print() throws PrintException {
		if (null == printer) {
			logger.error("未找到打印机：" + printerName);
			return false;
		}
		if (null == printDoc) {
			logger.error("未设置要打印的内容");
			return false;
		}
		// 如果打印机、打印内容全部设置正确，开始打印
		DocPrintJob job = printer.createPrintJob();//创建文档打印作业
		try {
			job.print(printDoc, aset);//打印文档
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return true;
	}

	public static void main(String args[]) {
		MiniPrinter printer = new MiniPrinter();
		printer.setPrinterName("XPS");// 设置要打印输出的打印机
		printer.setPsInFormat(DocFlavor.INPUT_STREAM.JPEG);// 设置格式
		printer.initPrinter();

		try {
			printer.setPrintDoc("d:\\1.TXT");
			printer.print();
		} catch (PrintException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
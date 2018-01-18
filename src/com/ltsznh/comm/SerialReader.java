package com.ltsznh.comm;

import gnu.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

public class SerialReader implements SerialPortEventListener {
	private Logger logger = LogManager.getLogger(this.getClass());

	private CommPortIdentifier portId;// 串口通信管理类
	private Enumeration portList;// 已经连接上的端口的枚举
	private SerialPort serialPort; // 串口的引用

	private InputStream inputStream;// 从串口来的输入流
	private OutputStream outputStream;// 向串口输出的流

	private int delayRead = 100;
	private int numBytes; // buffer中的实际数据字节数
	private static byte[] readBuffer = new byte[1024]; // 4k的buffer空间,缓存串口读入的数据


	private HashMap serialParams;
	private Thread readThread;//本来是static类型的
	//端口是否打开了
	boolean isOpen = false;

	// 端口读入数据事件触发后,等待n毫秒后再读取,以便让数据一次性读完

	public static final String PARAMS_PORT = "COM1"; // 端口名称
	public static final int PARAMS_DATABITS = SerialPort.DATABITS_8; // 数据位
	public static final int PARAMS_PARITY = SerialPort.PARITY_NONE; // 奇偶校验

	public static final int PARAMS_STOPBITS = SerialPort.STOPBITS_1; // 停止位

	public static final int PARAMS_BAUDRATE = 9600; // 波特率

	public static final int PARAMS_DELAY = 0; // 延时等待端口数据准备的时间
	public static final int PARAMS_TIMEOUT = 1000; // 超时时间


	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:

				try {
					int inputInt;//每次读出的数据
					while (inputStream.available() > 0) {
						inputInt = inputStream.read();

//						if (inputInt == 13) {
//							inputIndex = 0;
//							//处理数据,生成重量
//							StringBuffer sb = new StringBuffer();
//
//							for (int i = 9; i <= 14; i++) {
//								sb.append((char) dataInput[i]);//weight
//							}
//
////							try {
////								double d = Double.parseDouble(sb.toString());
////
//////							 for(int i =0;i < point;i++){
//////								  d = d / 10;
//////							 }
//////							 BigDecimal bd = new BigDecimal(d);
//////						     bd = bd.setScale(point, BigDecimal.ROUND_HALF_UP);
//////						     d =  bd.doubleValue();
////
////								StateMachine.weightContent = Format.getNumber(d, 3);
////
////								//将称上的值放到hashMap 里面
////								if (weightSeqNo != null && !weightSeqNo.equals("")) {
////									StateMachine.weightHm.put("weight" + weightSeqNo, Format.getNumber(d, 3));
////								}
////
////							} catch (Exception e) {
////								Log.logErr(e);
////							}
////							if (inputIndex == 60) {
////								inputIndex = 0;
////								break;
////							}
////						StateMachine.weightContent = sb.toString();
//							break;
//						}

//						dataInput[inputIndex] = inputInt;
//						inputIndex++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}


	/**
	 * 初始化串口
	 */
	public void init() {
		logger.info("端口：", PARAMS_PORT);
		logger.info("波特率：", PARAMS_BAUDRATE);
		logger.info("数据位：", PARAMS_DATABITS);
		logger.info("停止位：", PARAMS_STOPBITS);
		logger.info("校验位：", PARAMS_PARITY);

		logger.info("延迟：", PARAMS_DELAY);
		logger.info("超时：", PARAMS_TIMEOUT);

		// 获取串口列表
		getPortList();
		// 遍历串口，找到要读取的串口号
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(PARAMS_PORT)) {
					simpleRun();
				}
			}
		}
	}

	/**
	 * 打开端口，设置串口的波特率、数据位、停止位、校验位
	 */
	private void simpleRun() {
		// 打开串口
		try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 1000);
		} catch (PortInUseException e) {// 串口已经被占用
			logger.error(e);
		}
		// 获取串口输入流
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			logger.error(e);
		}

		//	监听串口事件,响应转到serialEvent()方法
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			logger.error(e);
		}

		serialPort.notifyOnDataAvailable(true);

		// 设置串口的波特率、数据位、停止位、校验位
		try {
			serialPort.setSerialPortParams(PARAMS_BAUDRATE, PARAMS_DATABITS, PARAMS_STOPBITS, PARAMS_PARITY);
		} catch (UnsupportedCommOperationException e) {
			logger.error(e);
		}

	}

	public CommPortIdentifier getPortId() {
		return portId;
	}

	public void setPortId(CommPortIdentifier portId) {
		this.portId = portId;
	}

	public Enumeration getPortList() {
		if (portList == null) {
			// 获取串口列表
			portList = CommPortIdentifier.getPortIdentifiers();
		}
		return portList;
	}

	public void setPortList(Enumeration portList) {
		this.portList = portList;
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public int getDelayRead() {
		return delayRead;
	}

	public void setDelayRead(int delayRead) {
		this.delayRead = delayRead;
	}

	public int getNumBytes() {
		return numBytes;
	}

	public void setNumBytes(int numBytes) {
		this.numBytes = numBytes;
	}

	public static byte[] getReadBuffer() {
		return readBuffer;
	}

	public static void setReadBuffer(byte[] readBuffer) {
		SerialReader.readBuffer = readBuffer;
	}

	public HashMap getSerialParams() {
		return serialParams;
	}

	public void setSerialParams(HashMap serialParams) {
		this.serialParams = serialParams;
	}

	public Thread getReadThread() {
		return readThread;
	}

	public void setReadThread(Thread readThread) {
		this.readThread = readThread;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean open) {
		isOpen = open;
	}

	public static String getParamsPort() {
		return PARAMS_PORT;
	}

	public static int getParamsDatabits() {
		return PARAMS_DATABITS;
	}

	public static int getParamsParity() {
		return PARAMS_PARITY;
	}

	public static int getParamsStopbits() {
		return PARAMS_STOPBITS;
	}

	public static int getParamsBaudrate() {
		return PARAMS_BAUDRATE;
	}

	public static int getParamsDelay() {
		return PARAMS_DELAY;
	}

	public static int getParamsTimeout() {
		return PARAMS_TIMEOUT;
	}
}
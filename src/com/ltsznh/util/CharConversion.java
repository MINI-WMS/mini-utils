package com.ltsznh.util;

public class CharConversion {

	// @SuppressWarnings("unused")
	// private String byteToHex(byte[] b) {
	// String hs = "";
	// String stmp = "";
	// for (int n = 0; n < b.length; n++) {
	// stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
	// if (stmp.length() == 1) {
	// hs = hs + "0" + stmp;
	// } else {
	// hs = hs + stmp;
	// }
	// }
	// return hs.toUpperCase();
	// }

	protected String byteToHex(byte[] bytes) {
		StringBuffer strbuf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
//			if (((int) bytes[i] & 0xff) < 0x10) {
//				strbuf.append("0");
//			}
//			strbuf.append(Long.toString((int) bytes[i] & 0xff, 16));
			strbuf.append(byteToHex(bytes[i]));
		}
		return strbuf.toString();
	}

	public static String byteToHex(byte b) {
		String str = "";
		if (((int) b & 0xff) < 0x10) {
			str = "0";
		}
		return str + Long.toString((int) b & 0xff, 16);
	}

	// @SuppressWarnings("unused")
	// private String byteArrayToHexString(byte[] b) {
	// StringBuffer resultSb = new StringBuffer();
	// for (int i = 0; i < b.length; i++) {
	// resultSb.append(byteToHexString(b[i]));
	// }
	// return resultSb.toString();
	// }

	// public static String byteToHexString(byte b) {
	// int n = b;
	// if (n < 0) {
	// n = 256 + n;
	// }
	// int d1 = n / 16;
	// int d2 = n % 16;
	// return hexDigits[d1] + hexDigits[d2];
	// }

	protected byte[] hexToByte(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		// hexString = hexString.toUpperCase(); //如果是大写形式
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			System.out.print(d[i]);
		}
		System.out.println("");
		return d;
	}

	private byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
		// return (byte) "0123456789ABCDEF".indexOf(c);
	}

	// private static final String[] hexDigits = { "0", "4", "5", "1", "2", "3",
	// "6",
	// "7", "d", "e", "f", "8", "9", "a", "b", "c" };

	// "0", "1", "2", "3", "4", "5", "6", "7","8", "9", "a", "b", "c", "d", "e",
	// "f"

	public static void main(String[] arg) {
		byte[] test = new byte[] { -100, -50, 50, 100 };
		CharConversion charConversion = new CharConversion();
		System.out.println(charConversion.byteToHex(test));
		byte[] tests = charConversion.hexToByte("999999");
		for (int i = 0; i < tests.length; i++) {
			System.out.println(tests[i]);
		}

	}
}

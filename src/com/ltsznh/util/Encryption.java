package com.ltsznh.util;


import com.ltsznh.model.ResultData;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.HashMap;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class Encryption {

	public Encryption() {

	}

	static {
		try {
			Security.addProvider(new BouncyCastleProvider());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String EncryptByMd5(String oldString) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();

			messageDigest.update(oldString.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	public static String encryptByMD5(String msg, String id) {
		String resultString = null;

		try {
			resultString = new String(msg + id);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	public static String EncryptByAES(String content, String password) {
		// KeyGenerator kgen = null;
		// byte[] result = null;
		// try {
		// kgen = KeyGenerator.getInstance("AES");
		// kgen.init(128, new SecureRandom(password.getBytes()));
		// SecretKey secretKey = kgen.generateKey();
		// byte[] enCodeFormat = secretKey.getEncoded();
		// SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		// Cipher cipher = Cipher.getInstance("AES");// 密码器
		// byte[] byteContent = content.getBytes("utf-8");
		// cipher.init(Cipher.ENCRYPT_MODE, key);
		// result = cipher.doFinal(byteContent);
		KeyGenerator kgen = null;
		String result = null;
		try {
			Security.addProvider(new BouncyCastleProvider());
			kgen = KeyGenerator.getInstance("AES");
			// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
			SecureRandom sr = null;
			// if (param != null
			// && Integer.parseInt(param.get("SDK_INT").toString()) > 16) {
			// sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			// } else {
			sr = SecureRandom.getInstance("SHA1PRNG");
			// }
			sr.setSeed(password.getBytes("utf-8"));
			kgen.init(128, sr); // 256 bits or 128 bits,192bits
			// kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			Base64 encoder = new Base64(); // 解决不同平台编码，造成加密、解密错误
			result = new String(encoder.encode(cipher.doFinal(byteContent)));
			// result = cipher.doFinal(byteContent);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// catch (NoSuchProviderException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return result;
	}

	// public static String DecryptByAES(String content, String password) {
	// return DecryptByAES(content, password);
	// }

	// public static String DecryptByAES_XML(String content,String password){
	// String[] pwdString = new String[content.length()];
	// int i = 0;
	// while(content.indexOf("_")>0){
	// pwdString[i] = content.substring(0, content.indexOf("_"));
	// content = content.substring(content.indexOf("_")+1);
	// i++;
	// }
	// byte[] pwdByte = new byte[i];
	// for(int j = 0;j < i;j++){
	// pwdByte[j] = (byte) Integer.parseInt(pwdString[j]);
	// }
	// KeyGenerator kgen;
	// byte[] result = null;
	// try {
	// kgen = KeyGenerator.getInstance("AES");
	// kgen.init(128,new SecureRandom(password.getBytes()));
	// SecretKey secretKey = kgen.generateKey();
	// byte[] enCodeFormat = secretKey.getEncoded();
	// SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	// Cipher cipher = Cipher.getInstance("AES");
	// cipher.init(Cipher.DECRYPT_MODE, key);
	// result = cipher.doFinal(pwdByte);
	// } catch (NoSuchAlgorithmException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NoSuchPaddingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (InvalidKeyException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalBlockSizeException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (BadPaddingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return new String(result);
	// }

	// public static String DecryptByAES(byte[] content, String password) {
	//
	// return new String(DecryptByAESByte(content, password));
	// }

	public static String DecryptByAESString(String content, String password) {
		return new String(DecryptByAES(content, password));
	}

	public static byte[] DecryptByAES(String content, String password) {
		KeyGenerator kgen;
		byte[] result = null;
		try {
			Base64 decoder = new Base64();
			byte[] src = decoder.decode(content);
			kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes("UTF-8"));
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] deCodeFormat = secretKey.getEncoded();
			SecretKey secretkey = new SecretKeySpec(deCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			result = cipher.doFinal(src);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static ResultData EncryptByAESToHexString(final HashMap<String, Object> param) {
		ResultData result = new ResultData();

		String strByte = EncryptByAES(param.get("str").toString(),
				param.getOrDefault("userCode", "userCode").toString());

		result.setMessage(strByte);
		return result;
	}

	public ResultData DecryptByAESHexString(final HashMap<String, Object> param) {
		ResultData result = new ResultData();

		result.setMessage(new String(
				DecryptByAES((param.get("str").toString()), param.getOrDefault("userCode", "userCode").toString())));
		return result;
	}

	public static String encrypt2SHA1(String p0) {
		if (p0 == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(p0.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	private static String encrypt(String inStr, MessageDigest md) {
		String out = null;
		try {
			byte[] digest = md.digest(inStr.getBytes());
			out = byte2Hex(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static String byte2Hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2Bytes(String s) {
		String s2;
		byte[] b = new byte[s.length() / 2];
		int i;
		for (i = 0; i < s.length() / 2; i++) {
			s2 = s.substring(i * 2, i * 2 + 2);
			b[i] = (byte) (Integer.parseInt(s2, 16) & 0xff);
		}
		return b;
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] + HEX_DIGITS[d2];
	}

	private final static String[] HEX_DIGITS = {
			// "0", "4", "5", "1", "2", "3", "6", "7", "d", "e", "f", "8", "9", "a",
			//			"b", "c"
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f"
	};

	public static void main(String[] argStrings) {
		// try {
		// Security.addProvider(new BouncyCastleProvider());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// HashMap<String, Object> param = new HashMap<String, Object>();
		// param.put("userCode", "0");
		// param.put("SDK_INT", 22);
		// param.put("str", EncryptByMd5("0"));
		// System.out.println(new Encryption().encryptByAES_String(param)
		// .getMessage());
		// param.put("str", new Encryption().encryptByAES_String(param)
		// .getMessage());
		//
		// System.out.println(new Encryption().decryptByAES_String(param)
		// .getMessage());
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("userCode", "0");
		param.put("str", "test");

		// System.out.println();
		System.out.println(EncryptByAESToHexString(param).getMessage());

		System.out.println(DecryptByAES(("67126134FCCF04CC94CE95F12E62F6FC"), "0").toString());
	}

}

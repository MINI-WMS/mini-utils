package com.ltsznh.util;


import com.ltsznh.model.ResultData;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.HashMap;

public class Encryption2 {
	public Encryption2() {

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

	public static byte[] EncryptByAES(String content, String password, HashMap<String, Object> param) {
		KeyGenerator kgen = null;
		byte[] result = null;
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
			sr.setSeed(password.getBytes());
			kgen.init(128, sr); // 256 bits or 128 bits,192bits
			// kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = cipher.doFinal(byteContent);

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
		return result;
	}

	public static ResultData EncryptByAES_HEX(String content, String password, HashMap<String, Object> param) {
		if (param.get("SDK_INT") == null)
			param.put("SDK_INT", 16);
		// StringBuffer str = new StringBuffer();
		byte[] strByte = EncryptByAES(content, param.get("userCode").toString() + password, param);
		// for (int i = 0; i < strByte.length; i++) {
		// str.append(strByte[i]);
		// str.append("_");
		// }
		ResultData result = new ResultData();
		result.setMessage(byte2hex(strByte));
		return result;
	}

	public static String DecryptByAES(String content, String password) {
		KeyGenerator kgen;
		byte[] result = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(content.getBytes());
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
		}
		return new String(result);
	}

	public static String DecryptByAES_HEX(String content, String password) {
		KeyGenerator kgen;
		byte[] result = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(hexStringToBytes(content));
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
		}
		return new String(result);
	}

	public static String DecryptByAES_HEX2(String content, String password, HashMap<String, Object> param) {

		KeyGenerator kgen = null;
		byte[] result = null;
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
			sr.setSeed(password.getBytes());
			kgen.init(128, sr); // 256 bits or 128 bits,192bits
			// kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(byteContent);
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
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(result);
	}

	public static String DecryptByAES_XML(String content, String password, HashMap<String, Object> param) {
		String[] pwdString = new String[content.length()];
		int i = 0;
		while (content.indexOf("_") > 0) {
			pwdString[i] = content.substring(0, content.indexOf("_"));
			content = content.substring(content.indexOf("_") + 1);
			i++;
		}
		byte[] pwdByte = new byte[i];
		for (int j = 0; j < i; j++) {
			pwdByte[j] = (byte) Integer.parseInt(pwdString[j]);
		}
		KeyGenerator kgen;
		byte[] result = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
			SecureRandom sr = null;
			// if (param != null
			// && Integer.parseInt(param.get("SDK_INT").toString()) > 16) {
			// sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			// } else {
			sr = SecureRandom.getInstance("SHA1PRNG");
			// }
			sr.setSeed(password.getBytes());
			kgen.init(128, sr); // 256 bits or 128 bits,192bits
			// kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(pwdByte);
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
		}
		return new String(result);
	}

	public static String DecryptByAES(byte[] content, String password) {
		// KeyGenerator kgen;
		// byte[] result = null;
		// try {
		// kgen = KeyGenerator.getInstance("AES");
		// kgen.init(128, new SecureRandom(password.getBytes()));
		// SecretKey secretKey = kgen.generateKey();
		// byte[] enCodeFormat = secretKey.getEncoded();
		// SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		// Cipher cipher = Cipher.getInstance("AES");
		// cipher.init(Cipher.DECRYPT_MODE, key);
		// result = cipher.doFinal(content);
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
		return new String(DecryptByAESByte(content, password));
	}

	public static byte[] DecryptByAESByte(byte[] content, String password) {
		KeyGenerator kgen;
		byte[] result = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(content);
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
		}
		return result;
	}

	public static String encryptSHA1(String p0) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		sha.update(p0.getBytes());
		return encrypt(p0, sha);
	}

	private static String encrypt(String inStr, MessageDigest md) {
		String out = null;
		try {
			byte[] digest = md.digest(inStr.getBytes());
			out = byte2hex(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	private static String byte2hex(byte[] b) {
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

	public static String encryptByMD5String(String msg, String id) {
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
		return hexDigits[d1] + hexDigits[d2];
	}

	private final static String[] hexDigits = { "0", "4", "5", "1", "2", "3", "6", "7", "d", "e", "f", "8", "9", "a",
			"b", "c" };

	// "0", "1", "2", "3", "4", "5", "6", "7","8", "9", "a", "b", "c", "d", "e",
	// "f"

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

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public ResultData encryptByAES_String(final HashMap<String, Object> param) {
		ResultData result = new ResultData();
		if (param.get("SDK_INT") == null)
			param.put("SDK_INT", 16);
		StringBuffer str = new StringBuffer();
		byte[] strByte = EncryptByAES(param.get("str").toString(), param.get("userId").toString(), param);
		for (int i = 0; i < strByte.length; i++) {
			str.append(strByte[i]);
			str.append("_");
		}
		result.setMessage(str.toString());
		return result;
	}

//	public ResultData decryptByAES_String(final HashMap<String, Object> param) {
//		ResultData result = new ResultData();
//		if (param.get("SDK_INT") == null)
//			param.put("SDK_INT", 16);
//		if (!param.containsKey("userId") || param.get("userId") == null) {
//			result = new UserManage().getUsers(param);
//			param.put("userId", result.getText(0, "用户ID"));
//		}
//		result = new ResultData();
//		result.setMessage(
//				new String(DecryptByAES_XML(param.get("str").toString(), param.get("userId").toString(), param)));
//		return result;
//	}

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

		// System.out.println();
		System.out.println(EncryptByAES_HEX("test", "test", param).getMessage());

		System.out.println(DecryptByAES_HEX("67126134FCCF04CC94CE95F12E62F6FC", "0test").toString());
	}
}

package com.ltsznh.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptString
{
  private static String encrypt(String inStr, MessageDigest md)
  {
    String out = null;
    try
    {
      byte[] digest = md.digest(inStr.getBytes());
      out = byte2hex(digest);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return out;
  }

  private static String byte2hex(byte[] b)
  {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++)
    {
      stmp = (Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1)
      {
        hs = hs + "0" + stmp;
      }
      else
      {
        hs = hs + stmp;
      }
    }
    return hs.toUpperCase();
  }

  public static String encryptSHA1(String p0) throws NoSuchAlgorithmException
  {
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    sha.update(p0.getBytes());
    return encrypt(p0, sha);
  }

  private final static String[] hexDigits =
      {
      "0", "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "a", "b", "c", "d", "e", "f"};

  /**
   * 转换字节数组为16进制字串
   * @param b 字节数组
   * @return 16进制字串
   */

  private static String byteArrayToHexString(byte[] b)
  {
    StringBuffer resultSb = new StringBuffer();
    for (int i = 0; i < b.length; i++)
    {
      resultSb.append(byteToHexString(b[i]));
    }
    return resultSb.toString();
  }

  private static String byteToHexString(byte b)
  {
    int n = b;
    if (n < 0)
    {
      n = 256 + n;
    }
    int d1 = n / 16;
    int d2 = n % 16;
    return hexDigits[d1] + hexDigits[d2];
  }

  public static String encryptMD5(String p0)
  {
    String resultString = null;

    try
    {
      resultString = new String(p0);
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return resultString;
  }
}
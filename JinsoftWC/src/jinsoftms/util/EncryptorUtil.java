package jinsoftms.util;

import java.security.MessageDigest;

public class EncryptorUtil {
	/**
	 * SHA1加密
	 * @param inStr
	 * @return
	 */
	public static String SHA1(String inStr) {
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		}
		sha1.update(inStr.getBytes());
		byte messageDigest[] = sha1.digest();
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < messageDigest.length; i++) {
			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	}
	
	/**
	 * MD5加密
	 * @param inStr
	 * @return
	 */
	public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
          md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
        	throw new RuntimeExceptionUtil(e.getMessage());
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; ++i)
          byteArray[i] = (byte)charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; ++i) {
          int val = md5Bytes[i] & 0xFF;
          if (val < 16)
            hexValue.append("0");
          hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
	}
}


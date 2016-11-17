package jinsoftms.util;

import java.util.Arrays;

public class CheckDataUtil {
	
	private static final String TOKEN = "jinsoft";
	
	public static boolean checkSignature(String signature,String timestamp,String nonce){
		String[] arr = new String[]{TOKEN,timestamp,nonce};
		
		//排序
		Arrays.sort(arr);
		
		//生成字符串
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<arr.length;i++){
			sb.append(arr[i]);
		}
		
		//sha1加密
		String temp = EncryptorUtil.SHA1(sb.toString());
		
		return temp.equals(signature);
	}
}

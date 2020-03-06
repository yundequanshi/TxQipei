package com.zuomei.weixin.pay;

/**
 * 加密处理类  
 * 
 * @author yuliang
 * 
 */  
public class EncryptUtils {

	public static String encode(String data, String pwd) throws Exception {
		byte[] bs = DESCoder.encryptDES(data.getBytes("GBK"), pwd);
		return Base64Utils.encrypt(bs);
	}

	public static String decode(String data, String pwd) throws Exception {
		byte[] bs = Base64Utils.decrypt(data);
		return new String(DESCoder.decryptDES(bs, pwd),"GBK");
	}

	public static void main(String[] args) {
		String key = "Q3i1Pei1";
		String data = "123456";
		try {
			String mdata = EncryptUtils.encode(data, key);
			mdata = EncryptUtils.decode(mdata, key);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

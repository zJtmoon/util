package com.creditease.honeybot.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
	
	/**
	 * 加密。加密后密文长度，被加密字符串在50字节内，密文长度在100字节内。
	 * @param sSrc
	 * @param sKey
	 * @return
	 */
	public static String encrypt(String sSrc, String sKey) {
        try{
        	if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
     
            return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
 
    /**
     * 解密
     * @param sSrc
     * @param sKey
     * @return
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,"utf-8");
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * 获取密钥
     * @param code
     * @return
     */
    public static String getKey(String code){
    	String key = null;
    	try {
    		if(code == null){
    			return null;
    		}
    		key = MD5Util.encrypt(code).substring(0, 16);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	
    	return key;
    }

	public static void main(String[] args) {
		System.out.println(AESUtil.decrypt("SY07h75vvWpSDMh71zwc+w==", getKey("password")));
	}

}

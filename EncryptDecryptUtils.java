package com.creditease.honeybot.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryptDecryptUtils {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static String decryptedString;
    private static String encryptedString;

    public EncryptDecryptUtils() {
    }

    public static void setKey(String myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest sha = null;
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }

    public static String getDecryptedString() {
        return decryptedString;
    }

    public static void setDecryptedString(String decryptedString) {
        decryptedString = decryptedString;
    }

    public static String getEncryptedString() {
        return encryptedString;
    }

    public static void setEncryptedString(String encryptedString) {
        encryptedString = encryptedString;
    }

    public static String encrypt(String strToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, secretKey);
        setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
        return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public static String decrypt(String strToDecrypt) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(2, secretKey);
        setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));
        return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
    }

    public static String convertToHexString(byte[] data) {
        StringBuffer strBuffer = new StringBuffer();

        for(int i = 0; i < data.length; ++i) {
            strBuffer.append(Integer.toHexString(255 & data[i]));
        }

        return strBuffer.toString();
    }
}

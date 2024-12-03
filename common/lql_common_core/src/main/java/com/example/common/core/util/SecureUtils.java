package com.example.common.core.util;

import cn.hutool.crypto.SecureUtil;

/**
 * 1、对称加密（symmetric），例如：AES、DES等
 * 2、非对称加密（asymmetric），例如：RSA、DSA等
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等
 */
public class SecureUtils {

    static String AES_encryptionKey = "hello，world！";

    public static String encryptionWithAES(String value){
        return SecureUtil.aes(AES_encryptionKey.getBytes()).encryptBase64(value);
    }
    public static String decryptionWithAES(String value){
        return SecureUtil.aes(AES_encryptionKey.getBytes()).decryptStr(value);
    }

    /**
     * 指定的秘钥解密
     * @param value 明文
     * @param key 秘钥
     * @return
     */
    public static String decryptionWithAES(String value,String key){
        return SecureUtil.aes(key.getBytes()).decryptStr(value);
    }

    public static void main(String[] args) {

        String s = encryptionWithAES("Ajxt520!");
        System.out.println(s);
        System.out.println(decryptionWithAES(s));
    }
}

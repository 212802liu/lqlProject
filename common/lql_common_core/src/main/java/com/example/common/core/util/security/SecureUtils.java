package com.example.common.core.util.security;

import cn.hutool.crypto.SecureUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    public static byte[] decryption(String value){
        return SecureUtil.aes(AES_encryptionKey.getBytes()).decryptStr(value).getBytes(StandardCharsets.UTF_8);
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

        String s = encryptionWithAES("DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBgth);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.lenggth);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.lenggth);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.lenggth);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.lenggth);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.lengytes.length);DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);");
        System.out.println(s);
//        System.out.println(decryptionWithAES(s));
//        System.out.println(decryption("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"));
    }
}

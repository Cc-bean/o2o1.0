package com.yyc.o2o.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * DES是一种对称加密算法（加密和解密使用相同密钥的算法）
 * @Auther:Cc
 * @Date: 2020/02/28/10:00
 */
public class DESUtil {
    private static Key key;
    //设置密钥key
    private static String KEY_STR="myKey";
    private static String CHARSETNAME="UTF-8";
    private static String ALGORITHM="DES";

    //静态代码块生成DES算法的实例
    static {
        try{
        //生成DES算法对象
        KeyGenerator generator=KeyGenerator.getInstance(ALGORITHM);
        //运用SHA1安全策略
        SecureRandom secureRandom= SecureRandom.getInstance("SHA1PRNG");
        //给策略设置上密钥种子
        secureRandom.setSeed(KEY_STR.getBytes());
        //初始化基于SHA1的算法对象
        generator.init(secureRandom);
        //生成密钥对象
        key=generator.generateKey();
        generator=null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取加密后的信息
     *@params:
     * @return
     */
    public static String getEncyptString(String str){
        //基于BASE64编码，接收byte[]并转换成String
        BASE64Encoder base64Encoder=new BASE64Encoder();
        try {
            //按utf8编码
            byte[] bytes=str.getBytes(CHARSETNAME);
            //获取加密对象
            Cipher cipher=Cipher.getInstance(ALGORITHM);
            //初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE,key);
            //加密
            byte[] doFinal=cipher.doFinal(bytes);
            //用base64Encoder将byte转换为String 并返回
            return base64Encoder.encode(doFinal);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static String getDecryptString(String str) {
        BASE64Decoder base64decoder = new BASE64Decoder();
        try {
            byte[] bytes = base64decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, CHARSETNAME);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {


    }
}

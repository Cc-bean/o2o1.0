package com.yyc.o2o.util.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信请求校验工具
 * @Auther:Cc
 * @Date: 2020/02/22/14:50
 */
public class SignUtil {
    //与接口配置信息中的Token="myo2o"
    private static String token="myo2o";
    /**
     * 文档连接：https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
     * 微信请求校验工具类
     * signature:微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp(时间戳)参数、nonce(随机数)参数。
     * 开发者通过检验signature对请求进行校验（下面有校验方式）。若确认此次GET请求来自微信服务器，请原样返回echostr(随机字符串)参数内容，则接入生效，成为开发者成功，否则接入失败。加密/校验流程如下：
     * 1）将token、timestamp、nonce三个参数进行字典序排序 2）将三个参数字符串拼接成一个字符串进行sha1加密 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     *@params:
     * @return
     */
    public static boolean checkSignature(String signature,String timestamp,String nonce){
        String[] arr=new String[]{token,timestamp,nonce};
        //将token,timestamp,nonce三个参数进行字典排序
        Arrays.sort(arr);
        //将数组按顺序拼接如字符串
        StringBuilder content=new StringBuilder();
        for(int i=0;i<arr.length;i++){
            content.append(arr[i]);
        }
        MessageDigest md=null;//MessageDigest来实现数据加密
        String tmpStr=null;
        try {
            md=MessageDigest.getInstance("SHA-1");
            //将拼接好的字符串进行sha.1加密
            byte[] digest=md.digest(content.toString().getBytes());
            tmpStr=byteToStr(digest);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        content=null;
        //将加密后的字符串与signature对比，标识改请求源于微信
        return tmpStr!=null?tmpStr.equals(signature.toUpperCase()):false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *@params:
     * @return
     */
    public static String byteToStr(byte[] byteArry){
        String strDigest="";
        for(int i=0;i<byteArry.length;i++){//获取字节数组中的每一个字节进行处理
            strDigest+=byteToHexStr(byteArry[i]);
        }
        return strDigest;
    }
    /**
     * 重写将字节转换为十六进制字符串
     *@params:
     * @return
     */
    private static String byteToHexStr(byte mByte){
        char[] Digt={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] tempArr=new char[2];
        //？？？？？？？？？？？？？？？
        tempArr[0]=Digt[(mByte>>>4) & 0X0F];
        tempArr[1]=Digt[mByte & 0X0F];
        String s=new String(tempArr);
        return s;
    }


}

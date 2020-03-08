package com.yyc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther:Cc
 * @Date: 2020/02/02/9:14
 */
public class HttpServletRequestUtil {
    //将Int型参数转换为Integer
    public static int getInt(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }
    //转换为长整型
    public static long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }
    //转换为双精度型
    public static Double getDouble(HttpServletRequest request,String key){
        try{
            return Double.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1d;
        }
    }
    //转化为布尔型
    public static boolean getBoolean(HttpServletRequest request,String key){
        try{
            return Boolean.valueOf(request.getParameter(key));
        }catch (Exception e){
            return false;
        }
    }
    //string类型处理
    public static String getString(HttpServletRequest request,String key){
        try{
            String result=request.getParameter(key);
            if(result!=null){
                result=result.trim();//如果不为空，去掉字符串两边空格；
            }
            if("".equals(result)){
                result=null;//如果没有值则复为空值
            }
            return result;

        }catch (Exception e){
            return null;//如果处理过程中有异常则返回null
        }
    }

}
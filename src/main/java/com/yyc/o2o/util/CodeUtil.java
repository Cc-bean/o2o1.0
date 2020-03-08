package com.yyc.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码验证工具类
 * @Auther:Cc
 * @Date: 2020/02/04/21:06
 */
public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request){
        String verifyCodeExpected= (String) request.getSession().
                getAttribute(Constants.KAPTCHA_SESSION_KEY);//从本次会话中获取验证码
        String verifyCodeActual=HttpServletRequestUtil.getString(request,"verifyCodeActual");//获取用户输入的验证码
        if(verifyCodeActual==null || !verifyCodeActual.equals(verifyCodeExpected.toUpperCase())){
            return false;
        }
        return true;
    }



}

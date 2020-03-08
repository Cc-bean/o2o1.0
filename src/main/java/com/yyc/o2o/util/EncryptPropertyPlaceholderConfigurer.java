package com.yyc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @Auther:Cc
 * @Date: 2020/02/28/11:22
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    //需要加密的字段数组
    private String [] encryPropNames={"jdbc.username","jdbc.password"};
    @Override
    protected String convertProperty(String propertyName, String propertyValue){
        if(isEncryProp(propertyName)){
            //对加密字段进行解密公众
            String decryptValue=DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        }else {
            return propertyValue;
        }

    }
    /**
     * 判断该属性是否已经加密
     *@params:
     * @return
     */
    private boolean isEncryProp(String propertyName){
        //若等于需要加密的field,则进行加密
        //遍历字符串数组
        for (String encryptpropertyName:encryPropNames) {
            if (encryptpropertyName.equals(propertyName))
                return true;
             }
            return false;
        }
}

package com.yyc.o2o.exceptions;

/**
 * @Auther:Cc
 * @Date: 2020/02/01/18:23
 */
public class ShopOperationException extends RuntimeException{
    //因为该类需要序列化，所以生成一个是序列化ID
    private static final long serialVersionUID = -891351847170275227L;

    public ShopOperationException(String msg){
        super(msg);
    }

}

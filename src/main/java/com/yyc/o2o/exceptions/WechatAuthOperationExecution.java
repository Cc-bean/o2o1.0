package com.yyc.o2o.exceptions;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/21:31
 */
public class WechatAuthOperationExecution extends RuntimeException{

    private static final long serialVersionUID = -8986446975011297012L;

    public WechatAuthOperationExecution(String msg){
        super(msg);
    }
}

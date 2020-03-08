package com.yyc.o2o.exceptions;

/**
 * @Auther:Cc
 * @Date: 2020/02/11/14:32
 */
public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = -9095980354005087932L;

    public ProductOperationException(String msg){
        super(msg);
    }


}

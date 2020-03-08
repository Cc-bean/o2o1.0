package com.yyc.o2o.exceptions;

/**
 * @Auther:Cc
 * @Date: 2020/03/03/11:51
 */
public class LocalAuthOperationException extends RuntimeException {


    private static final long serialVersionUID = 2729700718100817151L;

    public LocalAuthOperationException(String msg){
        super(msg);
    }

}

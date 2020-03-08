package com.yyc.o2o.exceptions;

/**
 * @Auther:Cc
 * @Date: 2020/02/10/11:32
 */
public class ProductCategoryOperationException extends RuntimeException{
    //避免序列化时uuid重复
    private static final long serialVersionUID = -7712728886666551236L;
    public ProductCategoryOperationException(String msg){
    super(msg);
    }

}

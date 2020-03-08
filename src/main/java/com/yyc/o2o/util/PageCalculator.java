package com.yyc.o2o.util;

/**
 * @Auther:Cc
 * @Date: 2020/02/08/10:40
 */
public class PageCalculator {
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
    }
}

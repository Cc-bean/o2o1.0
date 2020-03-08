package com.yyc.o2o.service;

import com.yyc.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/14/20:43
 */
public interface HeadLineService {
    public static final String HLLISTKEY="headlinelist";
    /**
     * 根据传入的条件返回指定的头条列表
     *@params:
     * @return
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition)throws IOException;




}

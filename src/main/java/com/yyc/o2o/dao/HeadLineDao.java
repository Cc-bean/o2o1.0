package com.yyc.o2o.dao;

import com.yyc.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/14/17:09
 */
public interface HeadLineDao {
    //由头条名查询头条
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);

}

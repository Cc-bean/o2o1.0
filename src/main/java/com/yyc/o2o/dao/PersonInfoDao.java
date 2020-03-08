package com.yyc.o2o.dao;

import com.yyc.o2o.entity.PersonInfo;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/16:19
 */
public interface PersonInfoDao {
    /**
     * 通过用户Id查用户
     *@params:
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);
    /**
     * 添加用户信息
     *@params:
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);




}

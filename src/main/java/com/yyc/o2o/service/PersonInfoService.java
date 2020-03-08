package com.yyc.o2o.service;

import com.yyc.o2o.entity.PersonInfo;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/21:45
 */
public interface PersonInfoService {
    /**
     * 根据用户Id获取personInfo信息
     *@params:
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);
}

package com.yyc.o2o.service.impl;

import com.yyc.o2o.dao.PersonInfoDao;
import com.yyc.o2o.entity.PersonInfo;
import com.yyc.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/21:47
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private PersonInfoDao personInfoDao;
    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}

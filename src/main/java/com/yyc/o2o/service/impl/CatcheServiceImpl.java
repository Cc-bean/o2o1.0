package com.yyc.o2o.service.impl;

import com.yyc.o2o.cache.JedisUtil;
import com.yyc.o2o.service.CatcheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Auther:Cc
 * @Date: 2020/03/01/20:35
 */
@Service
public class CatcheServiceImpl implements CatcheService {
   @Autowired
   private JedisUtil.Keys jedisKeys;
    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keySet=jedisKeys.keys(keyPrefix+"*");
        for(String key:keySet){
            jedisKeys.del(key);
        }
    }
}

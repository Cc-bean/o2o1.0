package com.yyc.o2o.service;

/**
 * 依据key前缀删除匹配该模式下的所有key-value
 * @Auther:Cc
 * @Date: 2020/03/01/20:32
 */

public interface CatcheService {

    void removeFromCache(String keyPrefix);


}

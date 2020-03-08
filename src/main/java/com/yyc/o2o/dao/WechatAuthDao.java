package com.yyc.o2o.dao;

import com.yyc.o2o.entity.WechatAuth;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/16:23
 */
public interface WechatAuthDao {
    /**
     * 通过openId查询对应平台的微信账号
     *@params:
     * @return
     */
    WechatAuth queryWechatInfoByOpenId(String openId);
    /**
     * 添加对应平台的微信账号
     *@params:
     * @return
     */
    int insertWechatAuth(WechatAuth wechatAuth);
}

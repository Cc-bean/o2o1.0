package com.yyc.o2o.service;

import com.yyc.o2o.dto.WechatAuthExecution;
import com.yyc.o2o.entity.WechatAuth;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/17:47
 */
public interface WechatAuthService {
    /**
     * 通过openId查找平台对应微信号
     *@params:
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);
    /**
     * 注册本平台微信账号
     *@params:
     * @return
     */
    WechatAuthExecution register(WechatAuth wechatAuth)throws RuntimeException;
}

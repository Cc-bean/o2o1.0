package com.yyc.o2o.service;

import com.yyc.o2o.dto.LocalAuthExecution;
import com.yyc.o2o.entity.LocalAuth;
import com.yyc.o2o.exceptions.LocalAuthOperationException;

/**
 * @Auther:Cc
 * @Date: 2020/03/03/11:46
 */
public interface LocalAuthService {
    /**
     * 通过账号和密码获取平台账号信息
     *@params:
     * @return
     */
    LocalAuth getLocalAuthByUsernameAndPwd(String userName,String password);
    /**
     * 通过userId获取平台账号信息
     *@params:
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);
    /**
     * 绑定微信，生成平台专属账号
     *@params:
     * @return
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;
    /**
     * 修改平台账号的登录密码
     *@params:
     * @return
     */
    LocalAuthExecution modityLocalAuth(Long userId,String userName,String password, String newPassword)throws LocalAuthOperationException;
}

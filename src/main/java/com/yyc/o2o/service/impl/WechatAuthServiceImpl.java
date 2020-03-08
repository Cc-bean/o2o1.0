package com.yyc.o2o.service.impl;

import com.yyc.o2o.dao.PersonInfoDao;
import com.yyc.o2o.dao.WechatAuthDao;
import com.yyc.o2o.dto.WechatAuthExecution;
import com.yyc.o2o.entity.PersonInfo;
import com.yyc.o2o.entity.WechatAuth;
import com.yyc.o2o.enums.WechatAuthStateEnum;
import com.yyc.o2o.exceptions.WechatAuthOperationExecution;
import com.yyc.o2o.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Auther:Cc
 * @Date: 2020/02/26/20:24
 */
@Service
public class WechatAuthServiceImpl implements WechatAuthService {
   private static Logger log= LoggerFactory.getLogger(WechatAuthServiceImpl.class);
   @Autowired
   private WechatAuthDao wechatAuthDao;
   @Autowired
   private PersonInfoDao personInfoDao;

   //通过openId获取用户信息
    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }
    //微信用户注册
    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws RuntimeException {
        //控制判断
        if(wechatAuth==null ||wechatAuth.getOpenId()==null){
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try{
            //设置创建时间
            wechatAuth.setCreateTime(new Date());
            //如果微信账号里带有用户信息且用户id为空，则认为用户第一次使用平台，且是微信登录
            //自动创建用户信息
            if(wechatAuth.getPersonInfo()!=null && wechatAuth.getPersonInfo().getUserId()==null){
             try {
                 wechatAuth.getPersonInfo().setCreateTime(new Date());
                 wechatAuth.getPersonInfo().setEnableStatus(1);
                 PersonInfo personInfo=wechatAuth.getPersonInfo();
                 int effectedNum=personInfoDao.insertPersonInfo(personInfo);
                 wechatAuth.setPersonInfo(personInfo);
                 if(effectedNum<=0){
                     throw new WechatAuthOperationExecution("添加用户信息失败");
                 }
             }catch (Exception e){
                 log.error("insertPerson error"+e.toString());
                 throw new WechatAuthOperationExecution("insertPersonInfo error"+e.getMessage());
             }
            }
            //创建专属本平台的的微信账号
            int effectedNum=wechatAuthDao.insertWechatAuth(wechatAuth);
            if(effectedNum<=0){
                throw new WechatAuthOperationExecution("账号创建失败");
            }else {
                //创建成功返回成功信息
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
            }
        }catch (Exception e){
        log.error("insertPerson error"+e.toString());
        throw new WechatAuthOperationExecution("insertWechatAuth error"+e.getMessage());
        }
    }
}

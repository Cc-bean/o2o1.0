package com.yyc.o2o.dao;

import com.yyc.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 通过账号和密码查询对应信息，登录使用
 * @Auther:Cc
 * @Date: 2020/03/02/20:29
 */
public interface LocalAuthDao {
     //通过账号密码查询对应信息
    /**
     *@params:
     * @return
     */
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username,@Param("password") String password);
    /**
     * 通过用户Id查询对应localauth
     *@params:
     * @return
     */
    LocalAuth queryLocalByUserId(@Param("userId") long userId);
    /**
     * 添加平台账号
     *@params:
     * @return
     */
    int insertLocalAuth(LocalAuth localAuth);
    /**
     * 通过userId，username,password更改密码
     *@params:
     * @return
     */
    int updateLocalAuth(@Param("userId") Long userId,@Param("username") String username,
                        @Param("password") String password,@Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);
}

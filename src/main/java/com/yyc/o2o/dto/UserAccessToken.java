package com.yyc.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信返回来的信息，由该实体类获取··WechatUser（微信用户实体类）实体类的信息
 * @Auther:Cc
 * @Date: 2020/02/22/20:52
 */
public class UserAccessToken {
    //JsonProperty：用杰克森将获取到的json字符串转化成实体类对应字段的值
    //获取到凭证
    @JsonProperty("access_token")
    private String accessToken;
    //凭证有效时间，单位：秒
    @JsonProperty("expires_in")
    private String expiresIn;
    //表示更新令牌，用来获取下一次的访问令牌（作用不大）
    @JsonProperty("refresh_token")
    private String refreshToken;
    //该用户在此公众号下的身份标识，对于此微信号具有唯一性
    @JsonProperty("openid")
    private String openId;
    //表示权限范围，这里可省略
    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

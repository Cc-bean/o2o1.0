package com.yyc.o2o.web.wechat;

import com.yyc.o2o.dto.UserAccessToken;
import com.yyc.o2o.dto.WechatAuthExecution;
import com.yyc.o2o.dto.WechatUser;
import com.yyc.o2o.entity.PersonInfo;
import com.yyc.o2o.entity.WechatAuth;
import com.yyc.o2o.enums.WechatAuthStateEnum;
import com.yyc.o2o.service.PersonInfoService;
import com.yyc.o2o.util.weixin.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 获取关注公众号之后的微信用户信息接口
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxbcc2772220f2ce7f&redirect_uri=http://www.cco2o.top/myo2o/wechatlogin/logincheck&roleType=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 在微信浏览器中访问对应url（微信官方文档提供）则会获取到code，之后可以通过code获取access_token进而获取用户信息
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 * @author lixiang
 *
 */
    @Controller
    @RequestMapping("wechatlogin")
    public class WechatLoginController {

	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

	@Resource
	private PersonInfoService personInfoService;
	@Resource
	private com.yyc.o2o.service.WechatAuthService WechatAuthService;

//	@Resource
//	private ShopService shopService;
//	@Resource
//	private ShopAuthMapService shopAuthMapService;

	private static final String FRONTEND = "1";
	private static final String SHOPEND = "2";

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		//获取微信公众号传输过来的code，通过code获取access_token,进而获取用户信息
		String code = request.getParameter("code");
		//state可以用来传我们自定义的信息，方便程序调用，这里可以不用
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WechatAuth auth = null;
		WechatUser user = null;
		String openId = null;
		if (null != code) {
			UserAccessToken token;//微信返回来的信息
			try {
			    //通过code获取acess_token
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = WechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		log.debug("weixin login success.");

		//若是第一次进入则创建账号
		if(auth==null){
			PersonInfo personInfo=WechatUtil.getPersonInfoFromRequest(user);
			auth=new WechatAuth();
			auth.setOpenId(openId);
			if(FRONTEND.equals(roleType)){
				personInfo.setUserType(1);
			}else {
				personInfo.setUserType(2);
			}
			auth.setPersonInfo(personInfo);
			WechatAuthExecution we=WechatAuthService.register(auth);
			if(we.getState()!= WechatAuthStateEnum.SUCCESS.getState()){
				return null;
			}
		}
		PersonInfo personInfo=personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
			request.getSession().setAttribute("user",personInfo);
		if(FRONTEND.equals(roleType)){
			return "frontend/index";
		}else {
			return "shop/shoplist";
		}

	}
}

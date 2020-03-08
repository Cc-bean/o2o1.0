package com.yyc.o2o.util.weixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyc.o2o.dto.UserAccessToken;
import com.yyc.o2o.dto.WechatUser;
import com.yyc.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;

/**
 * @Auther:Cc
 * @Date: 2020/02/23/16:04
 */
public class WechatUtil {
    private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

/**
 * 获取UserAccessToken实体类
 *@params:
 * @return
 */
public static UserAccessToken getUserAccessToken(String code) throws IOException {
    String appId = "wxbcc2772220f2ce7f";
    log.debug("appId:" + appId);
    String appsecret = "210e71281f5c1118f74160153e7db991";
    log.debug("secret:" + appsecret);
    //根据传入的cpode，拼接出访问微信定义好的接口url
    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
            + appId + "&secret=" + appsecret + "&code=" + code
            + "&grant_type=authorization_code";
    //向相应的url发送请求获取token json字符串
    String tokenStr = httpsRequest(url, "GET", null);
    log.debug("userAccessToken:" +tokenStr);
    UserAccessToken token=new UserAccessToken();
    ObjectMapper objectMapper=new ObjectMapper();
    try{
        //将json字符串转换成相应对象
        token=objectMapper.readValue(tokenStr,UserAccessToken.class);
    }catch (Exception e){
        log.error("获取用户信息失败："+e.getMessage());
    }

    if (token == null) {
        log.debug("获取用户accessToken失败。");
        return null;
    }
    return token;
}
/**
 * 访问url，获取信息
 * 发起https请求并获取结构
 *SSL协议用来对http加密——>https
 *@params:
 * @return
 */
    public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
        StringBuilder buffer=new StringBuilder();
        try {
            //创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm={new MyX509TrustManager()};
            SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
            sslContext.init(null,tm,new java.security.SecureRandom());
            //从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            //请求URL
            URL url=new URL(requestUrl);
            //打开连接
            HttpsURLConnection httpURLConn=(HttpsURLConnection)url.openConnection();
            //加密转换操作
            httpURLConn.setSSLSocketFactory(ssf);
            httpURLConn.setDoOutput(true);
            httpURLConn.setUseCaches(false);
            //设置请求方式
            httpURLConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod))httpURLConn.connect();

            //当有数据需要提交时
            if(null!=outputStr){
                OutputStream outputStream=httpURLConn.getOutputStream();
                //设置编码防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //将返回的输入流转换成字符流
            InputStream inputStream=httpURLConn.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

            String str=null;
            while((str=bufferedReader.readLine())!=null){
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            //释放资源
            inputStream.close();
            inputStream=null;
            httpURLConn.disconnect();
            log.debug("http buffer:"+buffer.toString());
        }catch (ConnectException ce){
            log.error("weixin sever connection timed out");
        }catch (Exception e){
            log.error("https request error{}",e.getMessage());
        }
        return buffer.toString();//将获取到的数据返回到前台
    }


    public static WechatUser getUserInfo(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + accessToken + "&openid=" + openId + "&lang=zh_CN";
        String userinfo = httpsRequest(url, "GET", null);
        ObjectMapper objectMapper=new ObjectMapper();
        WechatUser user = new WechatUser();
        try{
            //将json字符串转换成相应对象
            user=objectMapper.readValue(userinfo,WechatUser.class);
        }catch (Exception e){
            log.error("获取用户信息失败："+e.getMessage());
        }
        return user;
    }

//    public static boolean validAccessToken(String accessToken, String openId) {
//        String url = "https://api.weixin.qq.com/sns/auth?access_token="
//                + accessToken + "&openid=" + openId;
//        JSONObject jsonObject =httpsRequest(url, "GET", null);
//        int errcode = jsonObject.getInt("errcode");
//        if (errcode == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * 将WechatUser信息转换为personinfo信息
     *@params:
     * @return
     */
    public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(user.getNickname());
        personInfo.setGender(user.getSex() + "");
        personInfo.setProfileImg(user.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }
}







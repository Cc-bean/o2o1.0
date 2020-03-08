package com.yyc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyc.o2o.dto.ShopExecution;
import com.yyc.o2o.entity.Area;
import com.yyc.o2o.entity.PersonInfo;
import com.yyc.o2o.entity.Shop;
import com.yyc.o2o.entity.ShopCategory;
import com.yyc.o2o.enums.ShopStateEnum;
import com.yyc.o2o.service.AreaService;
import com.yyc.o2o.service.ShopCategoryService;
import com.yyc.o2o.service.ShopService;
import com.yyc.o2o.util.CodeUtil;
import com.yyc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:Cc
 * @Date: 2020/02/02/9:04
 */
@Controller
@RequestMapping("/shopadmin")//映射路径
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    //点击列表中的进入时跳转的html自动调用该方法获取点击的店铺的shopId,将id放入shop对象，将shop对象放入session
    @RequestMapping(value = "/getshopmanagementinfo",method=RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
       //工具类HttpServletRequestUtil
        long shopId=HttpServletRequestUtil.getLong(request,"shopId");
        if(shopId<=0){//判断如果前端没有传过来shopId,则从session中获取
            Object currentShopObj=request.getSession().getAttribute("currentShop");
            if(currentShopObj==null){//如果从session中获取仍然为空则重定向为原来界面
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o_war_exploded/shopadmin/shoplist/");
            }else {
                Shop currentShop=(Shop)currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {//若前端传过来shopId
            Shop currentShop=new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
            modelMap.put("shopId",currentShop.getShopId());

        }
        return modelMap;
    }
    /**
     * 查询店铺列表
     *@params:
     * @return
     */
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
      Map<String,Object> modelMap=new HashMap<String,Object>();
      //user.setUserId(1L);//防止session为空，就像初始化一个session
      //user.setName("test");
        PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
      try {
          Shop shopCondition=new Shop();
          shopCondition.setOwner(user);
          ShopExecution se=shopService.getShopList(shopCondition,0,100);
          modelMap.put("shopList",se.getShopList());
          modelMap.put("user",user);
          modelMap.put("success",true);
      }catch (Exception e){
          modelMap.put("success",false);
          modelMap.put("errMsg",e.getMessage());
      }
      return modelMap;
    }






    /**
     * 查询原来的店铺信息
     *@params:
     * @return
     */
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody//将返回值转换为json串
    private Map<String,Object> getShopById(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        Long shopId=HttpServletRequestUtil.getLong(request,"shopId");
        if(shopId>-1){
            try{
            Shop shop=shopService.getByShopId(shopId);
            List<Area> areaList=areaService.getAreaList();
            modelMap.put("shop",shop);
            modelMap.put("areaList",areaList);//店铺分类不能修改，所以只显示创建时的选择不显示其列表
            modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    /**
     * 更新店铺信息
     *@params:[request]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        System.out.println("触发修改方法------------");
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误！");
            return modelMap;
        }
        //1,接收并转化相应的参数，包括店铺信息以及图片信息
        //（1）接收字符信息。释：接收前端传过来的店铺相关的字符串的信息，并将这些信息转化为shop实体类
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //（2）接收处理图片信息。释：用spring自带的CommonsMultipartFile接收图片
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()//从request本次会话的上下文中获取相关文件上传的内容
        );//文件上传解析器解析request里面的信息
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        if ((CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg")!=null) {//判断commonsMultipartResolver是否有上传的文件流
            //如果有上传文件流，则将request转换为MultipartHttpServletRequest对象,再从该对象中提取出相应的文件流
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");//再将它转换为spring可以处理的文件流；shopImg前端图片name
        }
        //2，修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se;
            if (shopImg==null){
                se = shopService.modifyShop(shop, null);
            }else {
                se = shopService.modifyShop(shop, shopImg);
            }
            if(se.getState()== ShopStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
            }

            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺Id");
            return modelMap;
        }
    }

    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    /**
     * 将区域和店铺类别列表传回前端
     *@params:[]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap=new HashMap<String,Object>();//定义返回值
        List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();//接收数据库查询信息
        List<Area> areaList=new ArrayList<Area>();//接收数据库查询信息
        try {
            shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());//new ShopCategory()获取全部对象，就是先把数据库的每个都当成他的父亲，再让数据库找哪个id与他的parentId一样就是他真正的父亲
            areaList=areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsgg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody//将map返回值转换为json对象
    /**
     * 获取前端传来的店铺信息转换为实体类，获取前端传递过来的文件流，接收到shopimg中
     *@params:[request]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误！");
            return modelMap;
        }
        //1,接收并转化相应的参数，包括店铺信息以及图片信息
        //（1）接收字符信息。释：接收前端传过来的店铺相关的字符串的信息，并将这些信息转化为shop实体类
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //（2）接收处理图片信息。释：用spring自带的CommonsMultipartFile接收图片
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()//从request本次会话的上下文中获取相关文件上传的内容
        );//文件上传解析器解析request里面的信息
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        if ((CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg")!=null) {//判断commonsMultipartResolver是否有上传的文件流
            //如果有上传文件流，则将request转换为MultipartHttpServletRequest对象,再从该对象中提取出相应的文件流
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");//再将它转换为spring可以处理的文件流；shopImg前端图片name
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        //2，注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = new PersonInfo();
            //Session TODO
            owner = (PersonInfo) request.getSession()
                    .getAttribute("user");//取出session中的用户对象
            shop.setOwner(owner);
            ShopExecution se = shopService.addShop(shop, shopImg);
            if(se.getState()== ShopStateEnum.CHECK.getState()){
                modelMap.put("success",true);
                //设置该用户可以操作的列表
                //不进行警告
                @SuppressWarnings("unchecked")
                List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
                if(shopList==null || shopList.size()==0){
                    shopList=new ArrayList<Shop>();
                }
                shopList.add(se.getShop());
                request.getSession().setAttribute("shopList",shopList);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
            }

            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

}
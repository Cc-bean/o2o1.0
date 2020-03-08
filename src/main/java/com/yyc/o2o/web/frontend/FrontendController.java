package com.yyc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther:Cc
 * @Date: 2020/02/15/14:10
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {
    //首页
    @RequestMapping(value = "/index")
    public String shopOperation(){
        return "frontend/index";
    }
    //列表页
    @RequestMapping(value = "/shoplist")
    public String ShowShopList(){
        return "frontend/shoplist";
    }
    //店铺详情页
    @RequestMapping(value = "/shopdetail")
    public String showShopDetail(){
        return "frontend/shopdetail";
    }
    //商品详情页
    @RequestMapping(value = "/productdetail")
    public String showProductDetail(){
        return "frontend/productdetail";
    }


}

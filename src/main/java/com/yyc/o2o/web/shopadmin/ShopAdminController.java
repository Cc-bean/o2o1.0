package com.yyc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther:Cc
 * @Date: 2020/02/02/22:04
 */
@Controller
@RequestMapping(value = "/shopadmin",method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        return "shop/shopoperation";
    }
    //店铺列表页面
    @RequestMapping(value = "/shoplist")
    public String shopList(){
        return "shop/shoplist";
    }
    //店铺管理页面
    @RequestMapping(value = "/shopmanagement")
    public String shopManagement(){
        return "shop/shopmanagement";
    }
    //商品类别管理页面
    @RequestMapping(value = "/productcategorymanagement")
    public String productCategoryManage(){
        return "shop/productcategorymanagement";
    }
    //商品添加/编辑页面
    @RequestMapping(value = "/productoperation")
    public String productOperation(){
        return "shop/productoperation";
    }
    //商品管理页面
    @RequestMapping(value = "/productmanagement")
    public String productManagement(){
        return "shop/productmanagement";
    }
}








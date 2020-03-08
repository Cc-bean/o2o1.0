package com.yyc.o2o.web.frontend;

import com.yyc.o2o.entity.HeadLine;
import com.yyc.o2o.entity.ShopCategory;
import com.yyc.o2o.service.HeadLineService;
import com.yyc.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:Cc
 * @Date: 2020/02/14/20:50
 */
@Controller
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired//处理店铺类别相关操作
    private ShopCategoryService shopCategoryService;
    @Autowired//处理头条相关操作
    private HeadLineService headLineService;
    @RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        try {
            //获取一级店铺类别列表信息
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        List<HeadLine> headLineList = new ArrayList<HeadLine>();
        try {
            //获取状态（1）可用的头条列表
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("headLineList", headLineList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }

}

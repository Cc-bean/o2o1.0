package com.yyc.o2o.service;

import com.yyc.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/03/16:58
 */
public interface ShopCategoryService {
    public static final String SCLISTKEY="shopcategorylist";
    //查询商铺分类列表
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}

package com.yyc.o2o.service;

import com.yyc.o2o.dto.ShopExecution;
import com.yyc.o2o.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @Auther:Cc
 * @Date: 2020/02/01/15:28
 */
public interface ShopService {
    //返回条件下的店铺信息;需要工具类，将页数转换为行数，与dao层对应
    public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);

    //通过店铺Id获取店铺信息
    Shop getByShopId(long shopId);

    //更新店铺信息，包括图片处理
    ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) throws RuntimeException;

    //添加商铺
    ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) throws RuntimeException;

}

package com.yyc.o2o.dao;

import com.yyc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/01/30/14:25
 */
public interface ShopDao {

    /**
     * 分页查询店铺,可输入的条件有：店铺名（模糊），店铺状态，店铺Id,店铺类别,区域ID
     *
     * @param shopCondition
     * @param rowIndex//分页开始的行数
     * @param pageSize//该页显示行数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
    //查询出queryShopList总数
    int queryShopCount(@Param("shopCondition") Shop shopCondition);
    //通过shop id 查询店铺
    Shop queryByShopId(Long shopId);
    //新增店铺
    int insertShop(Shop shop);
    //跟新店铺信息
    int updateShop(Shop shop);


}

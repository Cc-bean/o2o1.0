package com.yyc.o2o.dao;

import com.yyc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/09/10:58
 */
public interface ProductCategoryDao {
    /**
     * 通过shop id查询店铺商品类别
     *@params:shopId
     * @return List<ProductCategory>
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /**
     * 批量新增商品类别
     *@params:
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);
    /**
     * 根据商品类别id与shopId删除
     * 将本店铺shopId也传入，使删除更安全，防止删除其他店铺的类别id
     *@params:
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId") long shopId);


}

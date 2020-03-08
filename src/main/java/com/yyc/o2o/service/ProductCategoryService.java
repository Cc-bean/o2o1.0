package com.yyc.o2o.service;

import com.yyc.o2o.dto.ProductCategoryExecution;
import com.yyc.o2o.entity.ProductCategory;
import com.yyc.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/09/16:04
 */
public interface ProductCategoryService {
    /**
     * 查询商品分类列表
     *
     * @return
     * @params:
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 插入商品列表
     *
     * @return
     * @params:
     */
    ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException;

    /**
     * 将此类别下的商品里的类别id置为空，再删除商品类别
     * @return
     * @params:
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;
}
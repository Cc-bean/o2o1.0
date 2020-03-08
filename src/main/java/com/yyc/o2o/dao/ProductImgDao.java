package com.yyc.o2o.dao;

import com.yyc.o2o.entity.ProductImg;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/11/9:36
 */
public interface ProductImgDao {
    /**
     * 查询商品图片列表
     *@params:
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);
    /**
     * 批量添加图片
     *@params:
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);
    /**
     * 通过商品Id删除图片
     *@params:
     * @return
     */
    int deleteProductImgByProductId(Long productId);


}

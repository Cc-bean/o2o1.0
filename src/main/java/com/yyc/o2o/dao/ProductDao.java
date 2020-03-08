package com.yyc.o2o.dao;

import com.yyc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/11/9:34
 */
public interface ProductDao {
    /**
     * 1，插入商品
     *@params:
     * @return
     */
    int insertProduct(Product product);
    /**
     * 2，由id查询商品信息
     *@params:
     * @return
     */
    Product queryProductById(long productId);
    /**
     *3， 查询商品数量
     *@params:
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 4，更新商品信息
     *@params:
     * @return
     */
    int updateProduct(Product product);
    /**
     * 5,查询商品列表并分页，条件：商品名，商品状态，店铺id,商品类别,分页
     *@params:
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition,@Param("rowIndex") int rowIndex,
                     @Param("pageSize") int pageSize);

    /**
     * 删除商品类别之前，将商品类别id置为空
     *@params:
     * @return
     */
     int updateProductCategoryToNull(long productCategoryId);
}

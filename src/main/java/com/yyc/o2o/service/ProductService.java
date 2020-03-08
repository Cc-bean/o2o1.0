package com.yyc.o2o.service;

import com.yyc.o2o.dto.ProductExecution;
import com.yyc.o2o.entity.Product;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/11/13:48
 */
public interface ProductService {
    //添加商品（商品信息+图片 ）
    ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail,
         List<CommonsMultipartFile> productImgs)throws IOException;
    //由Id获取商品信息
    Product getProductById(long productId);
    //修改商品信息
    ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
                                   List<CommonsMultipartFile> productImgs)throws IOException;
    //条件商品分页列表
    ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);


}

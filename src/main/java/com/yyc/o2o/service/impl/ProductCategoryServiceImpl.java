package com.yyc.o2o.service.impl;

import com.yyc.o2o.dao.ProductCategoryDao;
import com.yyc.o2o.dao.ProductDao;
import com.yyc.o2o.dto.ProductCategoryExecution;
import com.yyc.o2o.entity.ProductCategory;
import com.yyc.o2o.enums.ProductCategoryStateEnum;
import com.yyc.o2o.exceptions.ProductCategoryOperationException;
import com.yyc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/09/16:07
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
   @Autowired
   private ProductCategoryDao productCategoryDao;
   @Autowired
   private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        //先判断前端传过来的添加参数是否为空
        if(productCategoryList!=null && productCategoryList.size()>0){
           try{
            int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
            if(effectedNum<=0){//失败则抛出异常，成功则返回成功的信息
                throw new ProductCategoryOperationException("创建店铺失败");
            }else{
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch(Exception e){
               throw new ProductCategoryOperationException("batchAddProductCategory error:"+e.getMessage());
           }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional//因为该service有两步执行，所以用事务来管理
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
       //由于外键关联问题:将此商品类别下的商品的类别Id置为空
        try{
            int effectedNum=productDao.updateProductCategoryToNull(productCategoryId);
            if(effectedNum<0){
                throw new ProductCategoryOperationException("商品类别更新失败");
            }
        }catch (Exception e){
            throw new ProductCategoryOperationException("deleteProductCategory error"+e.getMessage());
        }
        //删除该productCategory
       int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId,shopId);
       try {
       if(effectedNum<=0){
           throw new ProductCategoryOperationException("商品类别删除失败");
       }else{
           return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
       }
       }catch (Exception e){
           throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
       }
    }
}

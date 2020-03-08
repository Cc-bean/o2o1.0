package com.yyc.o2o.web.shopadmin;

import com.yyc.o2o.dto.ProductCategoryExecution;
import com.yyc.o2o.dto.Result;
import com.yyc.o2o.entity.ProductCategory;
import com.yyc.o2o.entity.Shop;
import com.yyc.o2o.enums.ProductCategoryStateEnum;
import com.yyc.o2o.exceptions.ProductCategoryOperationException;
import com.yyc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:Cc
 * @Date: 2020/02/09/16:11
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;
    /**
     * Result与ShopExecution效果一样
     * 获取店铺下的商品列表
     *@params:
     * @return:Result<List<ProductCategory>>
     */
    @RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
        //currentShop在getShopManagementInfo方法中已经装入
        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        List<ProductCategory> list=null;
        if(currentShop!=null && currentShop.getShopId()>0){
            list=productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true,list);
        }else {
            ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
        }
    }
    /**
     * 前端插入店铺分类前端插入店铺分类
     * 前端参数新玩法，@RequestBody注解自动接收前端传来的参数
     *@params:[productCategoryList]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/addproductcategorys",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        for(ProductCategory pc:productCategoryList){
            pc.setShopId(currentShop.getShopId());
        }
        if(productCategoryList!=null && productCategoryList.size()>0){
            try{
                ProductCategoryExecution pe=productCategoryService.batchInsertProductCategory(productCategoryList);
                if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入至少一个商品类别");
        }
        return modelMap;
    }
    /**
     * 删除店铺分类
     *@params:[productCategoryId, request]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        if (productCategoryId!=null && productCategoryId>0){
            try{
                Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe=productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
                if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请选择至少一个商品类别");

        }
        return modelMap;
    }



}

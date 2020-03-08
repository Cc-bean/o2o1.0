package com.yyc.o2o.web.frontend;

import com.yyc.o2o.dto.ProductExecution;
import com.yyc.o2o.entity.Product;
import com.yyc.o2o.entity.ProductCategory;
import com.yyc.o2o.entity.Shop;
import com.yyc.o2o.service.ProductCategoryService;
import com.yyc.o2o.service.ProductService;
import com.yyc.o2o.service.ShopService;
import com.yyc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取店铺下的商品详情信息
 * @Auther:Cc
 * @Date: 2020/02/16/9:10
 */
@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
    //店铺信息
    @Autowired
    private ShopService shopService;
    //商品信息
    @Autowired
    private ProductService productService;
    //商品分类列表
    @Autowired
    private ProductCategoryService productCategoryService;
    /**
     * 商品详情信息
     *@params:
     * @return
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取前台传入的shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if (shopId != -1) {
            //获取该shopId下的信息
            shop = shopService.getByShopId(shopId);
            //获取商品下面的列表
            productCategoryList = productCategoryService .getProductCategoryList(shopId);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }
    /**
     * 依据查询条件分页列出该店铺下面的所有商品
     *@params:
     * @return
     */
    @RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取每页需要显示的条数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取店铺id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
            //尝试获取商品类别Id
            long productCategoryId = HttpServletRequestUtil.getLong(request,
                    "productCategoryId");
            //尝试获取模糊查询的商品名
            String productName = HttpServletRequestUtil.getString(request,
                    "productName");
            //组合查询条件
            Product productCondition = compactProductCondition4Search(shopId,
                    productCategoryId, productName);
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }
    //组合条件查询，将条件封装到productCondition对象返回
    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        //选出商品状态为1的商品上架
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}

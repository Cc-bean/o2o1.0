package com.yyc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyc.o2o.dto.ProductExecution;
import com.yyc.o2o.entity.Product;
import com.yyc.o2o.entity.ProductCategory;
import com.yyc.o2o.entity.Shop;
import com.yyc.o2o.enums.ProductStateEnum;
import com.yyc.o2o.service.ProductCategoryService;
import com.yyc.o2o.service.ProductService;
import com.yyc.o2o.util.CodeUtil;
import com.yyc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:Cc
 * @Date: 2020/02/12/8:34
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
@Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    //支持上传商品详情图最大数量
    private static final int IMAGEMAXCOUNT=6;
    /**
     *@params:
     * @return
     */
    @RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        //获取前台传过来的页码
        int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取返回的商品数
        int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
        //从当前session中获取店铺信息，主要是获取shopId
        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)){
            long productCategoryId=HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName=HttpServletRequestUtil.getString(request,"productName");
            Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            //传入条件查询
            ProductExecution pe=productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }



    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addproduct(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        //验证码验证
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }
        //接收前端参数的数量的初始化：商品，缩略图，详情图列表实体类
        ObjectMapper mapper=new ObjectMapper();
        Product product=null;
        //js代码中formData.append('productStr',JSON.stringify(product))
        String productStr= HttpServletRequestUtil.getString(request,"productStr");
        //接收处理图片信息。释：用spring自带的CommonsMultipartFile接收图片
        MultipartHttpServletRequest multipartRequest=null;
        CommonsMultipartFile thumbnail=null;//创建接收图片流的对象
        List<CommonsMultipartFile> productImgs=new ArrayList<CommonsMultipartFile>();//创建接收图片流数组的对象
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        try{
            if(multipartResolver.isMultipart(request)){
                // 转换request，解析出request中的文件
                multipartRequest=(MultipartHttpServletRequest) request;
                //在js中已经添加formData.append("thumbnail",thumbnail);
                thumbnail=(CommonsMultipartFile)multipartRequest.getFile("thumbnail");
                for (int i=0;i<IMAGEMAXCOUNT;i++){
                    CommonsMultipartFile productImg=(CommonsMultipartFile)multipartRequest.getFile("productImg"+i);
                    if (productImg!=null){
                        productImgs.add(productImg);
                    }else {
                        break;
                    }
                }
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try{
            //获取前端传过来的表单string转换为Product实体类
            product=mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if(thumbnail==null || productImgs.size()<=0){
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择商品图片");
        }else {
        if(product!=null && thumbnail!=null && productImgs.size()>0){
            try{
                Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
                Shop shop=new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                ProductExecution pe=productService.addProduct(product,thumbnail,productImgs);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
            }
        }
        return modelMap;
    }

    /**
     * 通过商品id获取商品信息
     *@params:
     * @return
     */
    @RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductById(HttpServletRequest request,@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //判断该商品是否属于该店铺
            //获取当前店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Long shopId=currentShop.getShopId();
       if(shopId!=productService.getProductById(productId).getShop().getShopId()) {
           modelMap.put("success",false);
           modelMap.put("errMsg","操作权限错误");
           return modelMap;
       }
        if (productId > -1) {
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService
                    .getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }
    /**
     * 修改商品信息
     *@params:
     * @return
     */
    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,
                "statusChange");
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request,
                "productStr");
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartFile thumbnail = null;
        List<CommonsMultipartFile> productImgs = new ArrayList<CommonsMultipartFile>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            thumbnail = (CommonsMultipartFile) multipartRequest
                    .getFile("thumbnail");
            for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                CommonsMultipartFile productImg = (CommonsMultipartFile) multipartRequest
                        .getFile("productImg" + i);
                if (productImg != null) {
                    productImgs.add(productImg);
                }else {
                    break;
                }
            }
        }
        try {
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (product != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution pe = productService.modifyProduct(product,thumbnail, productImgs);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }
    //条件查询时判断条件是否要加入
    private Product compactProductCondition(long shopId,long productCategoryId,String productName){
        Product productCondition=new Product();
        Shop shop=new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //判断是否要将指定要求添加进去
        if(productCategoryId!=-1L){
            ProductCategory productCategory=new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //判断是否要添加模糊查询的名字
        if(productName!=null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }
}

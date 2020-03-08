package com.yyc.o2o.service.impl;

import com.yyc.o2o.dao.ProductDao;
import com.yyc.o2o.dao.ProductImgDao;
import com.yyc.o2o.dto.ProductExecution;
import com.yyc.o2o.entity.Product;
import com.yyc.o2o.entity.ProductImg;
import com.yyc.o2o.enums.ProductStateEnum;
import com.yyc.o2o.exceptions.ProductOperationException;
import com.yyc.o2o.service.ProductService;
import com.yyc.o2o.util.ImageUtil;
import com.yyc.o2o.util.PageCalculator;
import com.yyc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther:Cc
 * @Date: 2020/02/11/14:52
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;


    /**
     * 1,处理缩略图，获取缩略图相对路径并赋值给product
     * 2,往tb_product写入商品信息，获取productId
     * 3,结合productId批量处理商品详情图
     * 4，将商品详情图列表批量插入tb_product_img中
     *@params:[product, thumbnail, productImgs]
     * @return com.yyc.o2o.dto.ProductExecution
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs)
    throws IOException{
        //空值判断
        if(product!=null && product.getShop()!=null && product.getShop().getShopId()!=null){
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
           //默认上架状态
            product.setEnableStatus(1);
            //如商品缩略图不为空则添加
            if(thumbnail!=null){
                addThumbnail(product,thumbnail);
            }
            try{
                //创建商品信息
                int effectedNum=productDao.insertProduct(product);
                if(effectedNum<0){
                    throw new ProductOperationException("创建商品失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品失败"+e.getMessage());
            }
            //添加商品详情图
            if(productImgs!=null&&productImgs.size()>0){
                addProductImgList(product,productImgs);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS,product);
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }
    /**
     * 由id获取商品信息
     *@params:
     * @return
     */
    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }
    /**
     * 修改商品信息
     * 1，若缩略图有值，则处理缩略图
     * 若原先存在缩略图则先删除再添加新图，之后获取缩略图路径并付给product
     * 2，若商品详情图列表有值，则对商品图片做相同操作
     * 3，将tb_product_img下商品详情图记录全部清除
     * 4，更新tb_product，tb_product_img信息
     *@params:
     * @return
     */
    @Override
    public ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs) throws IOException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setLastEditTime(new Date());
            if (thumbnail != null) {
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }
            //如果有新存入的商品详情图，则将原先的删除，再添加新图片
            if (productImgs != null && productImgs.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgs);
            }
            try {
                //更新商品信息
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败:" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }
    /**
     * 获取商品列表和数量
     *@params:
     * @return
     */
    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //将页码转换为数据库行码
        int rowIndex= PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList=productDao.queryProductList(productCondition,rowIndex,pageSize);
        //同样查询条件下的商品个数
        int count=productDao.queryProductCount(productCondition);
        ProductExecution pe=new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    /**
     * 添加缩略图
     *@params:
     * @return
     */
    private void addThumbnail(Product product,CommonsMultipartFile thumbnail) throws IOException {
        String dest= PathUtil.getShopImagePath(product.getShop().getShopId());//将商品图存与店铺下
        String thumbnailAddr= ImageUtil.generateThumbnaile(thumbnail,dest);//返回加过logo的图片地址
        product.setImgAddr(thumbnailAddr);//添加商品缩略图地址
        System.out.println();
        System.out.println();
    }
    private void addProductImgList(Product product, List<CommonsMultipartFile> productImgs) throws IOException {
        //获取图片存储路径，直接将图片存放到店铺相应的文件夹下
        String dest =PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList=new ArrayList<ProductImg>();
        //遍历图片流，放入productImgList里
        for(CommonsMultipartFile productimg:productImgs){
            String imgAddr=ImageUtil.generateNormalImg(productimg,dest);
            ProductImg productImg=new ProductImg();
            productImg.setImgDescaddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果确实有图片要添加，就执行批量添加操作
        if(productImgList.size()>0){
            try{
                int effectedNum=productImgDao.batchInsertProductImg(productImgList);
                if(effectedNum<=0){
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品详情图片失败："+e.getMessage());
            }
        }
    }
    /**
     * 删除某商品下的详情图
     *@params:
     * @return
     */
    private void deleteProductImgList(long productId){
        //根据productId获取原来的图片
        List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
        //删除原来的图片
        for(ProductImg productImg:productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgDescaddr());
        }
        //删除数据库原有图片信息
        productImgDao.deleteProductImgByProductId(productId);
    }

}

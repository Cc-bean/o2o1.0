package com.yyc.o2o.service.impl;

import com.yyc.o2o.dao.ShopDao;
import com.yyc.o2o.dto.ShopExecution;
import com.yyc.o2o.entity.Shop;
import com.yyc.o2o.enums.ShopStateEnum;
import com.yyc.o2o.exceptions.ShopOperationException;
import com.yyc.o2o.service.ShopService;
import com.yyc.o2o.util.ImageUtil;
import com.yyc.o2o.util.PageCalculator;
import com.yyc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @Auther:Cc
 * @Date: 2020/02/01/15:33
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired//注入dao层
    private ShopDao shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex= PageCalculator.calculateRowIndex(pageIndex,pageSize);//用工具类将页数转换为开始查询的行数
        List<Shop> shopList=shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        int count=shopDao.queryShopCount(shopCondition);
        ShopExecution se=new ShopExecution();
        if (shopList!=null){//如果该列表有数值，则将shoplist和状态添加进入返回结果中
            se.setShopList(shopList);
            se.setCount(count);
        }else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());

        }
        return se;
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    //更新店铺信息
    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg) throws RuntimeException {
        if(shop==null || shop.getShopId()==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            //1，判断是否需要处理图片
            try {
            if(shopImg!=null){
                Shop tempShop=shopDao.queryByShopId(shop.getShopId());
                if(tempShop.getShopImg()!=null){//不为空说明该ID下原来有图片，则需要先删除原来的再添加新的
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                }
                addShopImg(shop,shopImg);
            }
            //2，更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {// 创建成功
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new RuntimeException("modifyShop error: "
                        + e.getMessage());
            }
        }
    }


    @Override
    @Transactional//添加事务标签，声名支持事务
    public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) {
        //控制判断
        if(shop==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum=shopDao.insertShop(shop);
            if (effectedNum<=0){//当插入失败时，则抛出runtimeException,结束事务
                throw new ShopOperationException("店铺创建失败");
            }else {
                if(shopImg!=null){//当照片不为空时则添加图片
                    //存储图片，可能存在异常就catch一下，并抛出结束线程
                    try{
                        addShopImg(shop,shopImg);
                    }catch (Exception e){
                     throw new ShopOperationException("addShopImg error:"+e.getMessage());
                    }
                    //图片添加成功后，更新店铺图片地址
                    effectedNum=shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }

        }catch (Exception e){
            throw new ShopOperationException("addShop error");
        }
        //添加成功后返回待审核状态，并返回shop对象
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }
    //创建添加图片信息
    private void addShopImg(Shop shop, CommonsMultipartFile shopImg) throws IOException {
        //1，获取shop图片存储目录相对值路径
        String dest= PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr= ImageUtil.generateThumbnaile(shopImg,dest);
        shop.setShopImg(shopImgAddr);
    }

}


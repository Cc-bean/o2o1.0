package com.yyc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyc.o2o.cache.JedisUtil;
import com.yyc.o2o.dao.AreaDao;
import com.yyc.o2o.entity.Area;
import com.yyc.o2o.exceptions.AreaOperationException;
import com.yyc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
//声名AreaService是需要springIOC托管的
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisString;
    //key保存所有区域信息

    //引入日志
    private static Logger logger= LoggerFactory.getLogger(AreaServiceImpl.class);
    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key=AREALISTKEY;
        List<Area> areaList=null;
        //将arealist转化为strings（用json）存储在redis中
        ObjectMapper mapper=new ObjectMapper();
        if(!jedisKeys.exists(key)){//先判断该key在数据库中是否存在，不存在的话则添加进redis
            areaList=areaDao.queryArea();
            String jsonString;
            try {
                jsonString=mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            //将json string写入redis服务器
            jedisString.set(key,jsonString);
        }else {//如果redis中存在那么，从redis中取出后转换成area类型对象
            String jsonString=jedisString.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try {
                areaList=mapper.readValue(jsonString,javaType);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        //将strings转换为arealist对象返回前台
        return areaList;
    }


}








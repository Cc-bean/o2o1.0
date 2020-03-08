package com.yyc.o2o.web.supperadmin;

import com.yyc.o2o.entity.Area;
import com.yyc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
    //用sl4j调用logback主键（logger类的实例（由LoggerFactory类生成））
    Logger logger= LoggerFactory.getLogger(AreaController.class);
   @Autowired
   private AreaService areaService;
    @RequestMapping(value = "/listarea",method = RequestMethod.GET)
    @ResponseBody//将model对象自动转为json对象返回到前端
    private Map<String,Object> listArea(){
        logger.info("===start===");
        long startTime=System.currentTimeMillis();
        Map<String,Object> modelMap=new HashMap<String, Object>();
        List<Area> list=new ArrayList<Area>();
        try {
            list=areaService.getAreaList();
            modelMap.put("rows",list);
            modelMap.put("total",list.size());
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
        }
        logger.error("test error");
        long endTime=System.currentTimeMillis();
        //打印debug信息，输出花费时间
        logger.debug("costTime:[{}ms]",endTime-startTime);
        logger.info("===end===");
        return modelMap;
    }




}

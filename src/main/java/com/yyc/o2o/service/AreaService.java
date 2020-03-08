package com.yyc.o2o.service;

import com.yyc.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    public static final String AREALISTKEY="arealist";
//    查看地区分类列表
    List<Area> getAreaList();



}

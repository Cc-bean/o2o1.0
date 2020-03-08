package com.yyc.o2o.enums;

/**
 * @Auther:Cc
 * @Date: 2020/02/01/14:04
 */
public enum  ShopStateEnum {
    CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"ShopId为空"),NULL_SHOP(-1003,"shop信息为空");
    //因为不希望第三方程序改变0，-1，1，2，3这些值所以设置成私有的
    private int state;
    private String stateInfo;
    private ShopStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    /*
    * 依据传入的state返回相应的enum值
    * */
    public static ShopStateEnum stateOf(int state){
        for (ShopStateEnum stateEnum:values()){
            if(stateEnum.getState()==state){
                return stateEnum;
            }
        }
        return null;
    }
    //因为不希望程序外面调用set方法改变枚举的值，所以将set方法删掉
    public int getState() {
        return state;
    }


    public String getStateInfo() {
        return stateInfo;
    }

}

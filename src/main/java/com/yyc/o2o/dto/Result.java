package com.yyc.o2o.dto;

/**
 * 这是一个泛型类
 * 封装json对象，接收所有返回结果
 * @Auther:Cc
 * @Date: 2020/02/09/16:20
 */
public class Result<T> {
    private boolean success;//是否成功标志
    private T data;//成功时返回的数据
    private String errorMsg;//错误信息
    private int errorCode;//返回状态码

    public Result(){}

    //成功时构造器
    public Result(boolean success,T data){
        this.success=success;//  this.success(类的属性)=success(方法中的变量)——>给全局变量赋值
        this.data=data;
    }
    //错误时构造器
    public Result(boolean success,int errorCode,String errorMsg){
        this.success=success;
        this.errorMsg=errorMsg;
        this.errorCode=errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

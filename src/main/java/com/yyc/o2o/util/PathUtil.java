package com.yyc.o2o.util;

/**
 * @Auther:Cc
 * @Date: 2020/01/31/21:56
 */
public class PathUtil {
    //获取系统文件分隔符
    public static String seperator=System.getProperty("file.separator");
    //basePath路径处理(返回图片根路径)
    public static String getImgBasePath(){
    //获取系统名称
        String os=System.getProperty("os.name");
        String basePath="";
    if(os.toLowerCase().startsWith("win")){
        //windows系统路径
        basePath="C:/codeStore/ideaWorkspace/picture";
    }else {
        //其他系统路径
        basePath="/home/yyco2o/image/";
    }
        //将分隔符替换成该系统的分隔符
        basePath=basePath.replace("/",seperator);
        return basePath;
    }

    /**
     *返回项目图片子路径
     * 获取店铺图片存储路径
     *一个店铺一个路径（用shopId区分）
     */
    public static String getShopImagePath(long shopId){
        String imagePath="/upload/item/shop/"+shopId+"/";
        return imagePath.replace("/",seperator);
    }
}

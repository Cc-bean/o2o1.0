package com.yyc.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Auther:Cc
 * @Date: 2020/01/31/20:35
 */
public class ImageUtil {
    //获取classpath下的绝对值路径——>通过线程找到类加载器，从类加载器得到资源路径
    private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    //定义时间格式c.
    private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
    //定义随机对象
    private static final Random r=new Random();
    /**
     * 处理用户上传的图片，给文件重命名，加上水印后存入服务器本地
     *@params:用户传递过来的文件流，参数为spring自带的文件处理对象commonsMultipartFile和图片存储路径
     * @return
     * 该方法下的异常不应本类处理，需要抛出交给调用该方法的方法处理，让调用该方法的方法能够捕捉到该异常，以便进行事务回滚
     */
    public static String generateThumbnaile(CommonsMultipartFile thumbnail, String targetAddr)  throws IOException{
        String realFileName=getRandomFileName();//获取文件随即名方法
        String extension=getFileExtension(thumbnail);//获取后缀名
        makeDirPath(targetAddr);//创建图片存储路径
        String reativeAddr=targetAddr+realFileName+extension;
       // 生成新的String图片文件路径
        File dest=new File(PathUtil.getImgBasePath()+reativeAddr);
        //给缩略图加logo
        Thumbnails.of(thumbnail.getInputStream()).size(200,200)
                    .watermark(Positions.BOTTOM_LEFT,ImageIO.read
                                    (new File("C:\\codeStore\\ideaWorkspace\\picture\\upload\\item\\headtitle\\aaa.jpg"))
                            ,0.1f).
                outputQuality(0.9f).toFile(dest);
        return reativeAddr;
    }
    public static String generateNormalImg(CommonsMultipartFile thumbnail, String targetAddr)  throws IOException{
        String realFileName=getRandomFileName();//获取文件随即名方法
        String extension=getFileExtension(thumbnail);//获取后缀名
        makeDirPath(targetAddr);//创建图片存储路径
        String reativeAddr=targetAddr+realFileName+extension;
        // 生成新的String图片文件路径
        File dest=new File(PathUtil.getImgBasePath()+reativeAddr);
        //给缩略图加logo
        Thumbnails.of(thumbnail.getInputStream()).size(337,640)
                .watermark(Positions.BOTTOM_LEFT, ImageIO.read
                        (new File("C:\\codeStore\\ideaWorkspace\\picture\\upload\\item\\headtitle\\aaa.jpg")),0.1f).
                outputQuality(1.0f).toFile(dest);
        return reativeAddr;
    }
    /**
     * 创建目标路径上所涉及到的目录
     *@params:
     * @return
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
        File dirPath=new File(realFileParentPath);//创建存储图片的路径
        if(!dirPath.exists()){
            dirPath.mkdirs();//如果该路径不存在则创建
        }
    }

    /**
     * 获取输入文件流的扩展名
     *@params:
     * @return
     */
    private static String getFileExtension(CommonsMultipartFile cFile) {
        String originalFileName=cFile.getOriginalFilename();//获取文件名
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日时分秒+五位随机数
     *@params:
     * @return
     */
    public static String getRandomFileName(){
        //获取五位随机数,大于一万，小于89999
        int rannum=r.nextInt(89999)+10000;
        String nowTimeStr=sDateFormat.format(new Date());
        return nowTimeStr+rannum;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(basePath+"aaa.jpg");
       //原图片路径，定义宽高200,200，位置右下，logo路径timg.jpg，透明度0.25f，图片输出质量，形成的新图片路径
        Thumbnails.of(new File("C:\\development\\redis\\a.jpg")).
      size(1000,1000).watermark(Positions.BOTTOM_LEFT, ImageIO.read(new File(basePath+"aaa.jpg")).getSubimage(879,435,100,100),0.0f).
                outputQuality(0.9).toFile("C:\\development\\redis\\aq.jpg");
    }
    /**
     * 判断storePath是文件路径还是目录路径
     * 如果storePath是文件路径则删除该文件
     * 如果storePath是目录路径则删除该目录下所有文件
     *@params:String storePath
     * @return
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){//如果文件存在则进行删除操作
            if(fileOrPath.isDirectory()){//如果是目录路径，则删除目录下所有文件
                File files[]=fileOrPath.listFiles();
                for (int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            //删除玩目录下的文件，执行下面只一句也会将该目录删掉
            fileOrPath.delete();//如是文件路径则直接删除
        }



    }




}

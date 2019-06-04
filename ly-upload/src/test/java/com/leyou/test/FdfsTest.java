//package com.leyou.test;
//
//import com.github.tobato.fastdfs.domain.StorePath;
//import com.github.tobato.fastdfs.domain.ThumbImageConfig;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//import com.leyou.LyUploadServer;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//
///**
// * @ClassName FdfsTest
// * @Description TODO
// * @Author santu
// * @Date 2018/9/25 18:31
// * @Version 1.0
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LyUploadServer.class)
//public class FdfsTest {
//
//    @Autowired
//    private FastFileStorageClient storageClient;
//
//    @Autowired
//    private ThumbImageConfig imageConfig;
//
//    /**
//     * 测试上传图片
//     */
//    @Test
//    public void uploadTest() throws FileNotFoundException {
//        File file = new File("C:/Repository/preview.jpg");
//        // 上传
//        StorePath storePath = this.storageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
//        // 打印带分组路径
//        System.out.println(storePath.getFullPath());
//        // 打印不带分组的路径
//        System.out.println(storePath.getPath());
//
//    }
//
//    /**
//     * 生成缩略图
//     * @throws FileNotFoundException
//     */
//    @Test
//    public void uploadTest2() throws FileNotFoundException {
//        File file = new File("C:\\Repository\\preview.jpg");
//        // 上传
//        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), file.length(), "jpg", null);
//        // 打印带分组路径
//        System.out.println(storePath.getFullPath());
//        // 打印不带分组的路径
//        System.out.println(storePath.getPath());
//
//        // 缩略图的地址
//        String thumbImagePath = imageConfig.getThumbImagePath(storePath.getPath());
//        System.out.println(thumbImagePath);
//    }
//
//
//}

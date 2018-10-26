package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.service.IUploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName UploadServiceImpl
 * @Description TODO
 * @Author santu
 * @Date 2018/9/24 10:15
 * @Version 1.0
 **/
@Service
public class UploadServiceImpl implements IUploadService {

    /**
     * 创建日志对象
     */
    private  static final Logger logger =  LoggerFactory.getLogger(UploadServiceImpl.class);

    /**
     * 创建支持的文件类型集合
     */
    private static final List<String> sufixes = Arrays.asList("image/png","image/jpeg");
    
    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 文件上传 ,需要对文件进行校验: 1.文件大小,前台已校验 ;2.文件的媒体类型 ;3.文件的内容
     * @param file  : 文件保存的地址
     * @return      : 返回图片地址
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            // 首先校验文件类型是否支持
            String type = file.getContentType();
            if (!sufixes.contains(type)) {
                logger.info("上传失败,文件类型不匹配:{}",type);
                return null;
            }
            // 然后校验文件内容是否是图片
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                logger.info("上传失败,文件内容不符合要求");
                return null;
            }

            // 开始保存上传的图片
            // 获取上传文件的后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 上传文件至FastDFS
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            // 返回完整路径
            return "http://image.leyou.com/" + storePath.getFullPath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String uploadLocal(MultipartFile file) {
        try {
            // 首先校验文件类型是否支持
            String type = file.getContentType();
            if (!sufixes.contains(type)) {
                logger.info("上传失败,文件类型不匹配:{}",type);
                return null;
            }
            // 然后校验文件内容是否是图片
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                logger.info("上传失败,文件内容不符合要求");
                return null;
            }

            // 开始保存上传的图片
            File dir = new File("D:\\heima\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(dir, file.getOriginalFilename()));
            String url = "http://image.leyou.com/upload/" + file.getOriginalFilename();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

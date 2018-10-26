package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName IUploadService
 * @Description TODO
 * @Author santu
 * @Date 2018/9/24 10:15
 * @Version 1.0
 **/
public interface IUploadService {
    /**
     * 文件上传的service层,返回文件保存的地址
     * @param file  : 文件保存的地址
     * @return
     */
    String upload(MultipartFile file);

    String uploadLocal(MultipartFile file);
}

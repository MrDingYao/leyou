package com.leyou.controller;

import com.leyou.service.IUploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName UploadController
 * @Description 上传的后台controller
 * @Author santu
 * @Date 2018/9/24 10:15
 * @Version 1.0
 **/

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
        // 完成图片上传保存,返回一个保存的地址url
        String url = this.uploadService.upload(file);
        // 如果返回的url是空的,说明没有保存成功
        if (StringUtils.isBlank(url)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(url);
    }

    @PostMapping("local")
    public ResponseEntity<String> uploadLocal(@RequestParam("file") MultipartFile file){
        String url = this.uploadService.uploadLocal(file);
        if (StringUtils.isBlank(url)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(url);
    }
}

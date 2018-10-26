package com.leyou.page.controller;

import com.leyou.page.service.IGoodsService;
import com.leyou.page.service.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 17 16:30
 **/
@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private FileService fileService;

    /**
     * 跳转到商品详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(Model model,@PathVariable("id") Long id){
        Map<String,Object> modelMap = this.goodsService.loadModel(id);
        model.addAllAttributes(modelMap);
        // 判断是否存在，是否要生成静态化页面
        if (!this.fileService.exists(id)) {
            this.fileService.syncCreateHtml(id);
        }

        return "item";
    }
}

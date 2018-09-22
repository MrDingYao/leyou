package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @ClassName ICategoryService
 * @Description 商品分类查询的service接口
 * @Author santu
 * @Date 2018/9/21 14:57
 * @Version 1.0
 **/
public interface ICategoryService {

    /**
     * 通过父分类id查询其底下的子分类的集合
     * @param pid
     * @return
     */
    List<Category> queryListBiParent(Long pid);
}

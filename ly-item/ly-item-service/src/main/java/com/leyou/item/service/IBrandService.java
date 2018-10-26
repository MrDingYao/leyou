package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @ClassName IBrandService
 * @Description 品牌查询的service层的接口
 * @Author santu
 * @Date 2018/9/21 20:50
 * @Version 1.0
 **/
public interface IBrandService {

    /**
     * 通过前台传递过来的信息查询当前页面要显示的品牌信息,
     * @param page      : 当前的页码数
     * @param rows      : 页面显示的信息条数
     * @param sortBy    : 按照什么进行排序
     * @param desc      : 是否进行降序排序
     * @param key       : 输入搜索的关键字
     * @return
     */
    PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    /**
     * 增加一个商品品牌
     * @param brand
     * @param cids
     */
    void insertBrand(Brand brand, List<Long> cids);

    /**
     * 编辑一个商品品牌
     * @param brand
     * @param cids
     */
    void updateBrand(Brand brand, List<Long> cids);

    /**
     * 删除商品品牌brand
     * @param bid
     */
    void deleteBrand(Long bid);

    /**
     * 通过分类的id查询下属的品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandsByCid(Long cid);

    /**
     * 通过品牌id查询品牌
     * @param brandId
     * @return
     */
    Brand queryBrandByBid(Long brandId);

    /**
     * 通过品牌的id集合批量查询品牌
     * @param bids
     * @return
     */
    List<Brand> queryBrandByIds(List<Long> bids);
}

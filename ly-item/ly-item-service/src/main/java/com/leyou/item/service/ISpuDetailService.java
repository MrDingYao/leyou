package com.leyou.item.service;

import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 26 22:23
 **/
public interface ISpuDetailService {

    /**
     * 查询所有的商品详情的集合
     * @return
     */
    List<SpuDetail> queryAll();

    /**
     * 通过spuId查询对应的详情
     * @param id
     * @return
     */
    SpuDetail queryBySpuId(Long id);

    /**
     * 更新商品详情
     * @param s
     */
    void updateSpuDetail(SpuDetail s);
}

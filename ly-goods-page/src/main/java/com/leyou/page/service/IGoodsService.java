package com.leyou.page.service;

import java.util.Map;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 17 17:09
 **/
public interface IGoodsService {

    /**
     * 通过spuId来查询sku，规格参数等数据，并封装成map
     * @param id
     * @return
     */
    Map<String, Object> loadModel(Long id);
}

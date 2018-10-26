package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 15 23:41
 **/
public interface ISearchService {

    /**
     * 通过前台传递的条件查询符合条件的所有商品
     * @param request
     * @return
     */
    PageResult<Goods> search(SearchRequest request);

    /**
     * 接收到rabbitMQ发送来的insert和update消息后，更新索引库
     * @param id
     */
    void createIndex(Long id);

    /**
     * 接收到rabbitMQ发送来的delete消息后，删除索引
     * @param id
     */
    void deleteIndex(Long id);
}

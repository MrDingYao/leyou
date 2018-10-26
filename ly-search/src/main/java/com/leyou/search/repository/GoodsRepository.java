package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description // 导入数据使用
 * @Author santu
 * @Date 2018 - 10 - 13 19:49
 **/
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}

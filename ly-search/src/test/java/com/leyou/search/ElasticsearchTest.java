//package com.leyou.search;
//
//import com.leyou.LySearchService;
//import com.leyou.search.pojo.Goods;
//import com.leyou.search.repository.GoodsRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @Description //TODO
// * @Author santu
// * @Date 2018 - 10 - 13 19:50
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LySearchService.class)
//public class ElasticsearchTest {
//
//    @Autowired
//    private GoodsRepository goodsRepository;
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Test
//    public void createIndex() {
//        // 创建goods的索引
//        this.elasticsearchTemplate.createIndex(Goods.class);
//        // 创建map映射
//        this.elasticsearchTemplate.putMapping(Goods.class);
//    }
//}

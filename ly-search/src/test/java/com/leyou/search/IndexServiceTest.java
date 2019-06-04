//package com.leyou.search;
//
//import com.leyou.LySearchService;
//import com.leyou.common.pojo.PageResult;
//import com.leyou.item.pojo.SpuBo;
//import com.leyou.search.client.GoodsClient;
//import com.leyou.search.pojo.Goods;
//import com.leyou.search.repository.GoodsRepository;
//import com.leyou.search.service.IndexService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @Description //TODO
// * @Author santu
// * @Date 2018 - 10 - 14 13:55
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LySearchService.class)
//public class IndexServiceTest {
//
//    @Autowired
//    private IndexService indexService;
//
//    @Autowired
//    private GoodsClient goodsClient;
//
//    @Autowired
//    private GoodsRepository goodsRepository;
//
//    @Test
//    public void loadData() {
//        int page = 1;
//        int rows = 100;
//        int size = 0;
//        do {
//            PageResult<SpuBo> pageResult = this.goodsClient.querySpuByPage(null, true, page, rows);
//            List<SpuBo> spus = pageResult.getItems();
//            List<Goods> goodsList = spus.stream().map(s -> this.indexService.buildGoods(s)).collect(Collectors.toList());
//            this.goodsRepository.saveAll(goodsList);
//            size = spus.size();
//            page++;
//
//        } while (size == 100);
//    }
//
//    @Test
//    public void testQuery() {
//        PageResult<SpuBo> pageResult = this.goodsClient.querySpuByPage(null, null, 1, 5);
//        pageResult.getItems().forEach(System.out::println);
//
//    }
//}

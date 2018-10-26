package com.leyou.search;

import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 19 21:20
 **/
public class BeanTest {
    @Test
    public void name() {
        Goods goods = new Goods();
        Spu spu = new Spu();
        spu.setId(10L);
        spu.setCid1(1L);
        spu.setCid2(2L);
        spu.setCid3(3L);
        spu.setTitle("haha");
        BeanUtils.copyProperties(spu, goods);
        System.out.println(goods);
    }
}

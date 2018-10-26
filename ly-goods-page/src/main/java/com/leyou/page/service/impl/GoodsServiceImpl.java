package com.leyou.page.service.impl;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import com.leyou.page.service.IGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 17 17:10
 **/
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    /**
     * 通过spuId来查询sku，规格参数等数据，并封装成map
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> loadModel(Long id) {
        try {
            // 创建一个空的map
            Map<String,Object> modelMap = new HashMap<>();
            // 封装spu
            Spu spu = this.goodsClient.querySpuById(id);
            modelMap.put("spu",spu);
            // 封装spuDetail
            modelMap.put("spuDetail",this.goodsClient.querySpuDetailById(id));
            // 封装skus
            modelMap.put("skus",this.goodsClient.querySkuBySpuId(id));
            // 封装规格组specGroup
            modelMap.put("groups",this.specificationClient.querySpecsByCid(spu.getCid3()));
            // 封装品牌
            modelMap.put("brand", this.brandClient.queryBrandByIds(Collections.singletonList(spu.getBrandId())).get(0));
            // 封装特有规格参数
            List<SpecParam> specParams = this.specificationClient.querySpecParams(null, spu.getCid3(), null, false);
            // 将特有参数封装成id：name的键值对
            Map<Long,String> paramMap = new HashMap<>();
            specParams.forEach(s -> paramMap.put(s.getId(), s.getName()));
            modelMap.put("paramMap",paramMap);
            // 封装分类
            List<Category> categories = getCategories(spu);
            if (categories != null) {
                modelMap.put("categories", categories);
            }
            return modelMap;
        } catch (Exception e) {
            LOGGER.error("数据封装出错",e);
        }
        return null;
    }

    /**
     * 查询spu所属的分类集合
     * @param spu
     * @return
     */
    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            Category c1 = new Category();
            Category c2 = new Category();
            Category c3 = new Category();
            c1.setId(spu.getCid1());
            c1.setName(names.get(0));
            c2.setId(spu.getCid2());
            c2.setName(names.get(1));
            c3.setId(spu.getCid3());
            c3.setName(names.get(2));
            return Arrays.asList(c1,c2,c3);
        } catch (Exception e) {
            LOGGER.error("查询商品分类出错,spuId：{}",spu.getId(),e);
        }
        return null;
    }
}

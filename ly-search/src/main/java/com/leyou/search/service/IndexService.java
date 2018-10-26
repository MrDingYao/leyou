package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.*;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description // 创建商品的存储数据存入elasticsearch
 * @Author santu
 * @Date 2018 - 10 - 13 19:59
 **/
@Service
public class IndexService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 生成商品的存储数据存入elasticsearch
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu){
        Long id = spu.getId();
        // 1、准备数据
        // sku集合
        List<Sku> skus = this.goodsClient.querySkuBySpuId(id);
        // spuDetail
        SpuDetail detail = this.goodsClient.querySpuDetailById(id);
        // 商品分类
        List<String> names = this.categoryClient.queryNameByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 查询规格参数
        List<SpecParam> params = this.specificationClient.querySpecParams(
                null, spu.getCid3(), true, null);
        // TODO 查询品牌名称

        // 处理sku
        List<Long> prices = new ArrayList<>();
        List<Map<String,Object>> skuList = new ArrayList<>();
        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String,Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skuList.add(map);
        }

        // 处理规格参数
        Map<Long, String> genericMap = JsonUtils.parseMap(detail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(
                detail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
                });

        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            if(param.getGeneric()){
                // 通用参数
                String value = genericMap.get(param.getId());
                if(param.getNumeric()){
                    // 数值类型，需要存储一个分段
                    value = this.chooseSegment(value, param);
                }
                specs.put(param.getName(), value);
            }else{
                // 特有参数
                specs.put(param.getName(), specialMap.get(param.getId()));
            }
        }

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(id);
        goods.setSubTitle(spu.getSubTitle());
        // 搜索条件 拼接：标题、分类、品牌
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);

        return goods;
    }

    /**
     * 处理数字范围的参数信息
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


}

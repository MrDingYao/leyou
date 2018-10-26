package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 04 16:21
 **/
@Getter
@Setter
public class SpuBo extends Spu {

    /**
     * 分类名称
     */
    private String cname;

    /**
     * 品牌名称
     */
    private String bname;

    /**
     * 商品详情
     */
    private SpuDetail spuDetail;

    /**
     * sku列表
     */
    private List<Sku> skus;



}

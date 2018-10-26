package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 04 16:08
 **/
@Getter
@Setter
@Table(name = "tb_spu_detail")
public class SpuDetail {

    /**
     * 对应的spu的id
     */
    @Id
    private Long spuId;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品的全局规格属性
     */
    private String genericSpec;

    /**
     * 商品的特殊规格属性及可选值属性
     */
    private String specialSpec;

    /**
     * 包装清单
     */
    private String packingList;

    /**
     * 售后服务
     */
    private String afterService;

}

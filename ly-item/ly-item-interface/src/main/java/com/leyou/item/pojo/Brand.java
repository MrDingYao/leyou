package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName Brand
 * @Description 商品的品牌
 * @Author santu
 * @Date 2018/9/21 14:37
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * 品牌的名称
     */
    public String name;

    /**
     * 品牌的LOGO
     */
    public String image;

    /**
     * 品牌的首字母
     */
    public Character letter;
}

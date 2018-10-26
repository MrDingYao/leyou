package com.leyou.cart.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 23 19:43
 **/
@Getter
@Setter
public class Cart {

    private Long skuId;

    private String title;

    private Long price;

    private String image;

    private Integer num;

    private String ownSpec;


}

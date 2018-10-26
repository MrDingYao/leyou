package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 09 21:00
 **/
@Getter
@Setter
@Table(name = "tb_stock")
public class Stock {

    @Id
    private Long skuId;
    private Long seckillStock;      // 可秒杀库存
    private Long seckillTotal;      // 秒杀总数量
    private Integer stock;             // 库存数量

}

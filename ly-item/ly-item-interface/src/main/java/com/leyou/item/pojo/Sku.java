package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 09 20:53
 **/
@Getter
@Setter
@Table(name = "tb_sku")
public class Sku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String indexes;             // 商品特殊规格的下标
    private String ownSpec;             // 商品特殊规格的键值对
    private Boolean enable;             // 是否有效，逻辑删除
    private Date createTime;            // 创建时间
    private Date lastUpdateTime;        // 最后的修改时间

    @Transient
    private Integer stock;                 // 库存

}

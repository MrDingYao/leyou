package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Description tb_spu表格的实体类
 * @Author santu
 * @Date 2018 - 10 - 04 15:47
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_spu")
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long brandId;

    /**
     * 一级类目id
     */
    private Long cid1;

    /**
     * 二级类目id
     */
    private Long cid2;

    /**
     * 三级类目id
     */
    private Long cid3;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 是否上架
     */
    private Boolean saleable;

    /**
     * 是否有效，删除逻辑使用
     */
    private Boolean valid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后一次修改时间
     */
    private Date lastUpdateTime;

}

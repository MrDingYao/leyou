package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName Category
 * @Description 商品类目表
 * @Author santu
 * @Date 2018/9/21 14:31
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    /**
     * 分类id,主键
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类节点id,分类是多级分类,关联父节点id形成自关联表格,若为顶级分类,则为0
     */
    private Long parentId;

    /**
     * 是否为父类目
     */
    private Boolean isParent;

    /**
     * 排序指数,越小越靠前
     */
    private Integer sort;

}

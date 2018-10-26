package com.leyou.item.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@ToString
@Table(name = "tb_spec_group")
public class SpecGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属分类的id
     */
    private Long cid;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 改规格组下的所有规格参数集合
     */
    @Transient
    private List<SpecParam> params;
}

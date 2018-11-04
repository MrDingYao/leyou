package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 中国省市区表格
 * @Author santu
 * @Date 2018 - 10 - 30 17:14
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_province_city_down")
public class ProvinceCityDown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;         // 地区自身id

    private Integer pid;        // 所属的父id

    private String name;        // 名字
}

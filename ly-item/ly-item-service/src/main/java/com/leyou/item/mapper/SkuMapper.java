package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 09 21:16
 **/
public interface SkuMapper extends Mapper<Sku> , SelectByIdListMapper<Sku,Long> {
}

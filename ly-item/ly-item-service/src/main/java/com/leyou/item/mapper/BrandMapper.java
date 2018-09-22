package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName BrandMapper
 * @Description TODO
 * @Author santu
 * @Date 2018/9/21 19:21
 * @Version 1.0
 **/
public interface BrandMapper extends Mapper<Brand> {

    /**
     * tb_category_brand表添加数据
     * @param cid     : 分类category的id
     * @param bid    : 品牌brand的id
     */
    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);
}

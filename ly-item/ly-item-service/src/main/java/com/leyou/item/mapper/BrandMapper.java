package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName BrandMapper
 * @Description TODO
 * @Author santu
 * @Date 2018/9/21 19:21
 * @Version 1.0
 **/
public interface BrandMapper extends Mapper<Brand> , SelectByIdListMapper<Brand,Long> {

    /**
     * tb_category_brand表添加数据
     * @param cid     : 分类category的id
     * @param bid    : 品牌brand的id
     */
    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);


    /**
     * 删除tb_category_brand表的数据
     * @param bid
     */
    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    void deleteCategoryBrand(@Param("bid") Long bid);

    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{cid})")
    List<Brand> queryBrandsByCid(Long cid);
}

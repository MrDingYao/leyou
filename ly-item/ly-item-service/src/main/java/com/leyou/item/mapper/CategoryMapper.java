package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName CategoryMapper
 * @Description 商品分类信息查询的mapper,继承通用mapper
 * @Author santu
 * @Date 2018/9/21 15:00
 * @Version 1.0
 **/
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
    /**
     * 通过品牌的bid查询所属的分类的集合
     * tb_category_brand表和tb_category表联合查询
     * @param bid : 品牌的bid
     * @return
     */
    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);

    /**
     * 通过分类id查询品牌id
     * @param id
     * @return
     */
    @Select("select brand_id from tb_category_brand where category_id = #{id}")
    List<Long> queryByCid(Long id);

    /**
     * 通过分类id删除tb_category_brand表格的数据
     * @param id
     */
    @Delete("delete from tb_category_brand where category_id = #{id}")
    void deleteCategoryBrand(Long id);

}

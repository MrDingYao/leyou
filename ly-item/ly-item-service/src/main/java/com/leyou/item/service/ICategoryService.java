package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @ClassName ICategoryService
 * @Description 商品分类查询的service接口
 * @Author santu
 * @Date 2018/9/21 14:57
 * @Version 1.0
 **/
public interface ICategoryService {

    /**
     * 通过父分类id查询其底下的子分类的集合
     * @param pid
     * @return
     */
    List<Category> queryListBiParent(Long pid);

    /**
     * 通过品牌的bid查询所属的分类的集合
     * @param bid
     * @return
     */
    List<Category> queryByBrandId(Long bid);

    /**
     * 通过id集合查询所属的分类的集合
     * @param cids
     * @return
     */
    List<String> queryNamesByIds(List<Long> cids);

    /**
     * 新增分类
     * @param category
     */
    void insertCategory(Category category);

    /**
     * 编辑分类
     * @param category
     */
    void updateCategory(Category category);

    /**
     * 删除分类
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 通过分类cid查询tb_category_brand表格中的数据
     * @param id
     * @return
     */
    List<Long> queryInCategoryBrand(Long id);

    /**
     * 确认该分类是否关联其他数据
     * @param id
     * @return
     */
    String confirmDelete(Long id);

    /**
     * 通过第三级分类的id查询三级的分类
     * @param id
     * @return
     */
    List<Category> queryAllByCid3(Long id);
}

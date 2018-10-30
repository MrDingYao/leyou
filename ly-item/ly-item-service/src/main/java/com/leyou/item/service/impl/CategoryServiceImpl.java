package com.leyou.item.service.impl;

import com.leyou.common.utils.JsonUtils;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.ICategoryService;
import com.leyou.item.service.IGoodsService;
import com.leyou.item.service.ISpecificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName CategoryServiceImpl
 * @Description 商品分类信息查询的service层的实现类
 * @Author santu
 * @Date 2018/9/21 14:58
 * @Version 1.0
 **/
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ISpecificationService specificationService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 通过父分类id查询其底下的子分类的集合
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryListBiParent(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    /**
     * 通过品牌的bid查询所属的分类的集合
     * @param bid
     * @return
     */
    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 通过id集合查询所属的分类的名称集合
     * @param cids
     * @return
     */
    @Override
    public List<String> queryNamesByIds(List<Long> cids) {
        // CategoryMapper继承一个通用Mapper：SelectByIdListMapper，只有一个方法，即通过id集合查询所有的信息集合
        return this.categoryMapper.selectByIdList(cids)
                .stream().map(c -> c.getName()).collect(Collectors.toList());
    }

    /**
     * 新增分类
     * @param category
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertCategory(Category category) {
        // 先查询此category的父节点
        Category parentCategory = categoryMapper.selectByPrimaryKey(category.getParentId());
        // 然后判断父节点的isParent属性是否为true，不是则改为true
        if (!parentCategory.getIsParent()) {
            parentCategory.setIsParent(true);
            this.categoryMapper.updateByPrimaryKeySelective(parentCategory);
        }
        // 最后将新增的分类添加进数据库
        this.categoryMapper.insert(category);
    }

    /**
     * 编辑分类
     * @param category
     */
    @Override
    public void updateCategory(Category category) {
        this.categoryMapper.updateByPrimaryKeySelective(category);
    }

    /**
     * 删除分类及其相关联的数据
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategory(Long id) {
        // 先删除分类
        this.deleteCategoryAndEditParent(id);
        // 再删除tb_category_brand
        this.categoryMapper.deleteCategoryBrand(id);
        // 再删除specGroup表中的数据
        List<SpecGroup> specGroups = this.specificationService.querySpecGroups(id);
        specGroups.forEach(g -> this.specificationService.deleteGroup(g.getId()));
        // 再删除specParam表中的数据
        List<SpecParam> specParams = this.specificationService.querySpecParams(null, id, null, null);
        specParams.forEach(p -> this.specificationService.deleteParam(p.getId()));
        // 再删除spu表中的数据,修改valid字段属性为0，伪删除
        this.goodsService.deleteSpuByCid(id);

    }

    /**
     * 通过分类cid查询tb_category_brand表格中的数据
     * @param id
     * @return
     */
    @Override
    public List<Long> queryInCategoryBrand(Long id) {
        return this.categoryMapper.queryByCid(id);
    }

    /**
     * 确认该分类是否关联其他数据，是否可以直接删除
     * @param id
     * @return
     */
    @Override
    public String confirmDelete(Long id) {
        StringBuilder message = new StringBuilder();
        // 查询是否关联品牌
        List<Long> bids = this.queryInCategoryBrand(id);
        if (bids != null && bids.size() > 0) {
            message.append("该分类已关联品牌");
        }
        // 查询是否关联specGroup规格分组
        List<SpecGroup> specGroups = this.specificationService.querySpecGroups(id);
        if (specGroups != null && specGroups.size() > 0) {
            message.append(",规格分组");
        }
        // 查询是否关联specParam规格参数
        List<SpecParam> specParams = this.specificationService.querySpecParams(null, id, null, null);
        if (specParams != null && specParams.size() > 0) {
            message.append("，规格参数");
        }
        // 查询是否关联spu数据
        List<Spu> spus = this.goodsService.querySpuByCid(id);
        if (spus != null && spus.size() > 0) {
            message.append(",spu");
        }
        // 最后判断message是否为空，为空，说明没有关联，直接删除分类
        if (StringUtils.isBlank(message)) {
            this.deleteCategoryAndEditParent(id);
            return null;
        }
        // 否则返回message关联信息
        return message.append("等数据，是否确认全部删除？").toString();
    }

    /**
     * 删除分类并且判断其父节点是否还有其它子节点，若没有，则将父节点的isParent字段改为false
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategoryAndEditParent(Long id){
        // 先查询出该分类
        Category son = this.categoryMapper.selectByPrimaryKey(id);
        // 再查询出此分类的父分类
        Category parent = this.categoryMapper.selectByPrimaryKey(son.getParentId());
        // 再查询父分类之下的子节点的个数
        Category category = new Category();
        category.setParentId(parent.getId());
        List<Category> select = this.categoryMapper.select(category);
        // 若父亲的子节点只有此一个，则修改父分类的isParent属性为false，
        if (select != null && select.size() <= 1) {
            parent.setIsParent(false);
            this.categoryMapper.updateByPrimaryKeySelective(parent);
        }
        // 删除这个分类
        this.categoryMapper.delete(son);
    }

    /**
     * 通过第三级分类的id查询三级的分类
     * @param id
     * @return
     */
    @Override
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }

    /**
     * 查询所有的分类,使用redis
     * @return
     */
    @Override
    public List<Category> queryAllCategory() {
        List<Category> categoryList = new ArrayList<>();
        // 先从redis中取
        if (this.redisTemplate.hasKey("categories")) {
            String categories = this.redisTemplate.opsForValue().get("categories");
            categoryList = JsonUtils.parseList(categories,Category.class);
        }else {
            categoryList = this.categoryMapper.selectAll();
            this.redisTemplate.opsForValue().set("categories",JsonUtils.serialize(categoryList));
        }


        return categoryList;
    }
}

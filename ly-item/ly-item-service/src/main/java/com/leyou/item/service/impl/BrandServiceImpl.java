package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.IBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName BrandServiceImpl
 * @Description 品牌查询的service的实现类
 * @Author santu
 * @Date 2018/9/21 20:51
 * @Version 1.0
 **/
@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 通过前台传递过来的信息查询当前页面要显示的品牌信息,
     * @param page      : 当前的页码数
     * @param rows      : 页面显示的信息条数
     * @param sortBy    : 按照什么进行排序
     * @param desc      : 是否进行降序排序
     * @param key       : 输入搜索的关键字
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始设置分页
        PageHelper.startPage(page, rows);
        // 设置分页查询的条件
        Example example = new Example(Brand.class);
        // 按输入的关键字查询,两种情况:和name属性模糊查询或者和首字母精确查询
        if (StringUtils.isNotBlank(key)) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter",key.toUpperCase());
            example.and(criteria);
        }
        // 排序,判断desc参数是true的话进行降序DESC;是false的话进行升序ASC
        if (StringUtils.isNotBlank(sortBy)) {
            String orderClause = sortBy + (desc?" DESC":" ASC");
            example.setOrderByClause(orderClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<>(pageInfo.getTotal(),pageInfo);
    }

    /**
     * 增加商品品牌
     * @param brand : 输入的商品品牌信息
     * @param cids  : 该商品品牌所属的分类的id集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertBrand(Brand brand, List<Long> cids) {
        // 先在brand表中增加
        int row = brandMapper.insertSelective(brand);
        // 然后将brand表和category表之间的中间表tb_category_brand添加相对应的数据
        cids.forEach(c -> brandMapper.insertCategoryBrand(c,brand.getId()));

    }
}

package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.IBrandService;
import com.leyou.item.service.ICategoryService;
import com.leyou.item.service.IGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 04 16:31
 **/
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);


    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 设置查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 如果saleable不为空，设置上架与否的过滤
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }

        // 如果key不为空，则设置模糊查询，查询标题，分类和品牌
        if (StringUtils.isNotEmpty(key)) {
            criteria.orLike("title", "%" + key + "%");
        }
        // 按条件查询出页面信息
        Page<Spu> pageInfo = (Page<Spu>) this.goodsMapper.selectByExample(example);
        // 对页面信息进行处理，将Spu转成SpuBo对象，使用stream流
        List<SpuBo> collect = pageInfo.stream().map(spu -> {
            // 创建一个新的spuBo对象
            SpuBo spuBo = new SpuBo();
            // 复制spu的属性到spuBo对象中
            BeanUtils.copyProperties(spu,spuBo);
            // 查询spu商品所属的所有分类的名字,在categoryService类中添加一个方法
            List<String> cnames = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            // 将名字集合转化成字符串的格式
            spuBo.setCname(StringUtils.join(cnames, "/"));
            // 查询spu商品所属的品牌的名字
            Brand brand = this.brandService.queryBrandByBid(spu.getBrandId());
            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(),collect);
    }

    /**
     * 通过分类cid删除spu,其实是修改valid字段为0，伪删除
     * @param cid
     */
    @Override
    public void deleteSpuByCid(Long cid){
        // 查询出关联的spu的集合
        List<Spu> spus = this.querySpuByCid(cid);
        // 遍历集合，修改每一个spu的valid属性为0,并更新表的数据
        spus.forEach(s -> {
            s.setValid(false);
            this.goodsMapper.updateByPrimaryKeySelective(s);
        });
    }

    /**
     * 通过商品spuId 查询商品详情spuDetail
     * @param id
     * @return
     */
    @Override
    public SpuDetail querySpuDetailById(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过商品spuId 查询具体的sku集合
     * @param id
     * @return
     */
    @Override
    public List<Sku> querySkusById(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = this.skuMapper.select(sku);
        if (skus != null && skus.size() != 0) {
            // 同时查询sku的库存stock
            skus.forEach(s -> s.setStock(this.stockMapper.selectByPrimaryKey(s.getId()).getStock()));
        }
        return skus;
    }

    /**
     * 通过分类id查询spu的集合
     * @param id
     * @return
     */
    @Override
    public List<Spu> querySpuByCid(Long id) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orEqualTo("cid1",id);
        criteria.orEqualTo("cid2",id);
        criteria.orEqualTo("cid3",id);
        return this.goodsMapper.selectByExample(example);
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    @Override
    public void saveGoods(SpuBo spuBo) {
        // 添加spubo中的一些属性
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.goodsMapper.insert(spuBo);
        // 修改spuDetail的spuId
        spuBo.getSpuDetail().setSpuId(spuBo.getId());
        // 保存spuDetail
        this.spuDetailMapper.insert(spuBo.getSpuDetail());
        // 保存sku和stock库存信息
        this.saveSkuAndStock(spuBo.getSkus(),spuBo.getId());
        // 保存完毕后，发送消息
        this.sendMessage(spuBo.getId(),"insert");
    }

    /**
     * 修改商品spu和sku和库存stock
     * 新增新添的sku，修改没取消的sku，逻辑删除取消的sku
     * 修改spu的数据
     * @param spuBo
     */
    @Transactional
    @Override
    public void updateGoods(SpuBo spuBo) {
        // 获得更新的sku集合
        List<Sku> skus = spuBo.getSkus();
        if (!CollectionUtils.isEmpty(skus)) {
            // spu的id没变，查询原来的skus集合,将原来的所有sku设置成逻辑删除
            this.querySkusById(spuBo.getId()).stream().peek(s -> s.setEnable(false)).forEach(s -> {
                skuMapper.updateByPrimaryKeySelective(s);
            });
            // 筛选 新修改的sku集合，id为null的新增，id不为null的修改
            skus.stream().filter(s -> s.getId() != null).forEach(s -> {
                this.skuMapper.updateByPrimaryKeySelective(s);
            });

            skus.stream().filter(s -> s.getId() == null).forEach(s -> {
                s.setCreateTime(new Date());
                s.setLastUpdateTime(s.getCreateTime());
                s.setSpuId(spuBo.getId());
                this.skuMapper.insert(s);
            });

            // 新增对应的库存
            skus.forEach(this::saveStock);
        }

        // 更新spu数据
        spuBo.setLastUpdateTime(new Date());
        spuBo.setSaleable(null);
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        this.goodsMapper.updateByPrimaryKeySelective(spuBo);
        // 保存完毕后，发送消息
        this.sendMessage(spuBo.getId(),"update");
    }

    /**
     * 传入sku集合，新增库存信息
     * @param sku
     */
    private void saveStock(Sku sku) {
        Stock stock = new Stock();
        // 先删除原始的库存
        stock.setSkuId(sku.getId());
        this.stockMapper.delete(stock);
        // 保存stock库存信息
        stock.setSkuId(sku.getId());
        stock.setStock(sku.getStock());
        this.stockMapper.insert(stock);

    }

    /**
     * 传入sku集合和spu的id，保存sku信息和stock库存信息
     * @param skus
     * @param id
     */
    private void saveSkuAndStock(List<Sku> skus, Long id) {
        for (Sku sku : skus) {
            // 先判断sku是否是有效的
            if (!sku.getEnable()) {
                continue;
            }
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(id);
            this.skuMapper.insert(sku);
           saveStock(sku);
        }
    }

    /**
     * 上架和下架商品
     * @param id
     * @param flag  : 为true，则为上架，为false，则为下架
     */
    @Override
    public void shelfGoods(Long id,Boolean flag) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(flag);
        this.goodsMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 根据商品的spuId查询spu
     * @param id
     * @return
     */
    @Override
    public Spu querySpuById(Long id) {
        return this.goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据商品的skuId查询sku商品
     * @param id
     * @return
     */
    @Override
    public Sku querySkuBySkuId(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }

    /**
     * 当对商品进行增删改后，调用该方法发送消息
     * @param id
     * @param type
     */
    public void sendMessage(Long id,String type){
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}:商品消息发送失败，id：{}",type,id,e);
        }
    }
}

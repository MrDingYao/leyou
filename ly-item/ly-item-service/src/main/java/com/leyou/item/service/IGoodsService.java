package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 04 16:31
 **/
public interface IGoodsService {

    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key);

    /**
     * 通过商品spuId 查询商品详情spuDetail
     * @param id
     * @return
     */
    SpuDetail querySpuDetailById(Long id);

    /**
     * 通过商品spuId查询具体的sku集合
     * @param id
     * @return
     */
    List<Sku> querySkusById(Long id);

    /**
     * 通过分类cid删除spu
     * @param cid
     * @return
     */
    void deleteSpuByCid(Long cid);

    /**
     * 通过分类cid查询spu集合
     * @param id
     * @return
     */
    List<Spu> querySpuByCid(Long id);

    /**
     * 新增商品
     * @param spuBo
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 修改商品
     * @param spuBo
     */
    void updateGoods(SpuBo spuBo);

    /**
     * 上架和下架商品
     * @param id
     */
    void shelfGoods(Long id,Boolean flag);

    /**
     * 根据商品的spuId查询spu
     * @param id
     * @return
     */
    Spu querySpuById(Long id);

    /**
     * 根据商品的skuId查询sku
     * @param id
     * @return
     */
    Sku querySkuBySkuId(Long id);
}

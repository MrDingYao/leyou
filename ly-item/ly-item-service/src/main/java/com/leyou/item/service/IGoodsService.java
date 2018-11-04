package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;

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
     * 查询spu下的所有的sku，包括逻辑删除的
     * @param id
     * @return
     */
    List<Sku> queryAllSkusById(Long id);

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

    /**
     * 通过品牌id删除商品
     * @param bid
     */
    void deleteSpuByBid(Long bid);

    /**
     * 更新sku
     * @param s
     */
    void updateSku(Sku s);

    /**
     * 通过商品的spuId删除商品
     * @param id
     */
    void deleteSpuBySpuId(Long id);

    /**
     * 发送rabbitMQ消息的方法
     * @param id
     * @param type
     */
    void sendMessage(Long id,String type);

    /**
     * 修改商品库存
     * @param id
     * @param num
     * @return
     */
    Boolean updateStock(Long id, Integer num);

    /**
     * 通过spuId查询该spu下所有的sku的ownSpec集合
     * @param id
     * @return
     */
    List<String> queryOwnSpecs(Long id);

    /**
     * 通过spuId查询该spu下所有的sku的indexes集合
     * @param id
     * @return
     */
    List<String> queryAllIndexes(Long id);

    /**
     * 查询skuId集合对应的sku集合
     * @param ids
     * @return
     */
    List<Sku> querySkusBySkuIds(List<Long> ids);
}

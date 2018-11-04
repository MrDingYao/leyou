package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 12 15:52
 **/
public interface GoodsApi {

    /**
     * 分页查询商品
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<SpuBo> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                     @RequestParam(value = "saleable",required = false) Boolean saleable,
                                     @RequestParam(value = "page",defaultValue = "1") Integer page,
                                     @RequestParam(value = "rows",defaultValue = "5") Integer rows);

    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    /**
     * 通过商品spuId查询具体的sku集合
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    /**
     * 根据商品的spuId查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据商品的skuId查询sku商品
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    ResponseEntity<Sku> querySkuBySkuId(@PathVariable("id") Long id);

    /**
     * 查询spu下的所有有效的sku
     * @param id
     * @return
     */
    @GetMapping("sku/enabled/{id}")
    List<Sku> queryEnabledSkuBySpuId(@PathVariable("id") Long id);

    /**
     * 删减商品sku的库存
     * @return
     */
    @PutMapping("stock")
    Boolean updateStock(@RequestParam("id") Long id,@RequestParam("num") Integer num);

    /**
     * 通过spuId查询该spu下所有的sku的ownSpec集合
     * @param id
     * @return
     */
    @GetMapping("sku/ownSpecs/{spuId}")
    List<String> queryOwnSpecs(@PathVariable("spuId")Long id);

    /**
     * 通过spuId查询该spu下所有的sku的indexes集合
     * @param id
     * @return
     */
    @GetMapping("sku/indexes/{spuId}")
    List<String>queryAllIndexes(@PathVariable("spuId")Long id);
}

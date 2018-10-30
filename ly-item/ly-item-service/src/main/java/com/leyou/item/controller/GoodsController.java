package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.IGoodsService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 04 16:16
 **/
@RestController
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询商品列表
     * @param key       ： 搜索框内的搜索条件
     * @param saleable  ： 是否上架
     * @param page      ： 当前页
     * @param rows      ： 页面大小
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows){

        PageResult<SpuBo> pageResult = this.goodsService.querySpuByPage(page,rows,saleable,key);

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 通过spuId查询商品详情spuDetail
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id){
        SpuDetail spuDetail = this.goodsService.querySpuDetailById(id);
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 通过商品spuId查询具体的sku集合
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
        List<Sku> skus = this.goodsService.queryAllSkusById(id);
        return ResponseEntity.ok(skus);
    }

    /**
     * 新增商品
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        try {
            this.goodsService.saveGoods(spuBo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改商品
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> editGoods(@RequestBody SpuBo spuBo){
        try {
            this.goodsService.updateGoods(spuBo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 下架商品，修改spu表格中的saleable字段
     * @param id
     * @return
     */
    @PutMapping("shelfGoods")
    public ResponseEntity<Void> shelfGoods(@RequestParam("id") Long id,
                                           @RequestParam("flag") Boolean flag){
        this.goodsService.shelfGoods(id,flag);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 根据商品的spuId查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        if (spu == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(spu);
    }

    /**
     * 根据商品的skuId查询sku商品
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("id") Long id){
        Sku sku = this.goodsService.querySkuBySkuId(id);
        if (sku == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(sku);
    }


    /**
     * @Description TODO 删除商品功能，暂时没写，需要删除spu表，修改valid属性，删除spuDetail数据
     *              TODO 还要删除elasticSearch搜索服务中的数据
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id") Long id){
        try {
            this.goodsService.deleteSpuBySpuId(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 查询spu下的所有有效的sku
     * @param id
     * @return
     */
    @GetMapping("sku/enabled/{id}")
    public ResponseEntity<List<Sku>> queryEnabledSkuBySpuId(@PathVariable("id") Long id){
        List<Sku> skus = this.goodsService.querySkusById(id);
        if (CollectionUtils.isEmpty(skus)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(skus);
    }
}

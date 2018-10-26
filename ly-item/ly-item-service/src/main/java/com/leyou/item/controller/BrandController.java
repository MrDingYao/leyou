package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.common.utils.BeanUtils;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BrandController
 * @Description 查询商品品牌的controller
 * @Author santu
 * @Date 2018/9/21 14:57
 * @Version 1.0
 **/
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    /**
     * 通过前台传递过来的信息查询当前页面要显示的品牌信息,
     * @param page      : 当前的页码数
     * @param rows      : 页面显示的信息条数
     * @param sortBy    : 按照什么进行排序
     * @param desc      : 是否是降序
     * @param key       : 进行搜索的关键字
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                 @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                                 @RequestParam(value = "sortBy",required = false) String sortBy,
                                                 @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
                                                 @RequestParam(value = "key",required = false) String key){

        PageResult<Brand> pageResult = this.brandService.queryBrandByPageAndSort(page,rows,sortBy,desc,key);
        if (pageResult == null || pageResult.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 增加商品品牌
     * @param brand : 输入的商品对象
     * @param cids  : 该商品所属的分类的id集合
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.insertBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 编辑商品品牌
     * @param brand : 要修改的商品对象
     * @param cids  : 改商品所属的分类的cid集合
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> editBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.updateBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除商品品牌
     * @param bid
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteBrand(@RequestParam("bid") Long bid){
        this.brandService.deleteBrand(bid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 通过分类的id查询下属的品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid") Long cid){
        List<Brand> list = this.brandService.queryBrandsByCid(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 通过品牌id查询品牌
     * @param bid
     * @return
     */
    @GetMapping("{bid}")
    public ResponseEntity<Brand> queryBrandByBid(@PathVariable("bid") Long bid){
        Brand brand = this.brandService.queryBrandByBid(bid);
        return ResponseEntity.ok(brand);
    }

    /**
     * 通过品牌id集合批量查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        List<Brand> brands = this.brandService.queryBrandByIds(ids);
        if (BeanUtils.isNull(brands)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brands);
    }

}

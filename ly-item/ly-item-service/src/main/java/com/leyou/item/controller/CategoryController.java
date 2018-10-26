package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.ResultInfo;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.ICategoryService;
import com.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CategoryController
 * @Description 商品分类信息查询的controller层实现
 * @Author santu
 * @Date 2018/9/21 14:56
 * @Version 1.0
 **/
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 通过前台传递的所属的父分类的pid查询子分类集合
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        List<Category> list = this.categoryService.queryListBiParent(pid);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 通过前台传递的bid查询所属的分类集合
     * @param bid : 前台传递过来的品牌bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid){
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if (list == null || list.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCategory(Category category){
        this.categoryService.insertCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 编辑分类
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> editCategory(Category category){
        this.categoryService.updateCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除分类,及其相关联的数据
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id){
        this.categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 用来确认是否可以直接删除，查询该分类是否关联了其他的比如商品，品牌之类的数据
     * @param id
     * @return
     */
    @DeleteMapping("confirm/{id}")
    public ResponseEntity<String> confirmDelete(@PathVariable("id") Long id){
        String message = this.categoryService.confirmDelete(id);
        return ResponseEntity.ok(message);
    }

    /**
     * 通过分类的id查询分类的名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByids(@RequestParam("ids") List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(names);
    }

    /**
     * 通过第三级分类的id查询三级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> categories = this.categoryService.queryAllByCid3(id);
        if (CollectionUtils.isEmpty(categories)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }
}

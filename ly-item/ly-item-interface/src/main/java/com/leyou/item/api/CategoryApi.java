package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description  商品分类服务接口
 * @Author santu
 * @Date 2018 - 10 - 12 15:52
 **/
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 通过分类id集合查询分类名字集合
     * @param ids
     * @return
     */
    @GetMapping("names")
    List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 通过第三级分类的id查询三级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    List<Category> queryAllByCid3(@RequestParam("id") Long id);
}

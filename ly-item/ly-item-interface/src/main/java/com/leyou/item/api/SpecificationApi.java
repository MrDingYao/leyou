package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 13 21:06
 **/
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 查询具体组名下拥有的规格参数
     * 将此方法进行改进，可以通过gid，cid，searching（是否可搜索），generic（是否通用）进行搜索规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    List<SpecParam> querySpecParams(@RequestParam(value = "gid", required = false) Long gid,
                                    @RequestParam(value = "cid",required = false) Long cid,
                                    @RequestParam(value = "searching",required = false) Boolean searching,
                                    @RequestParam(value = "generic",required = false) Boolean generic);

    /**
     * 通过分类id查询其之下的规格参数组，并封装进具体的规格参数specParams数据
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);

}

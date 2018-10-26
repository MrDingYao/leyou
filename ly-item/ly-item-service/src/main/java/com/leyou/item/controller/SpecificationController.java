package com.leyou.item.controller;

import com.leyou.common.utils.BeanUtils;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private ISpecificationService specificationService;

    /**
     * 查询具体分类对应的规格集合
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.querySpecGroups(cid);
        return ResponseEntity.ok(groups);
    }

    /**
     * 查询具体组名下拥有的规格参数
     * 将此方法进行改进，可以通过gid，cid，searching（是否可搜索），generic（是否通用）进行搜索规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParams(@RequestParam(value = "gid", required = false) Long gid,
                                                           @RequestParam(value = "cid",required = false) Long cid,
                                                           @RequestParam(value = "searching",required = false) Boolean searching,
                                                           @RequestParam(value = "generic",required = false) Boolean generic){
        List<SpecParam> list = this.specificationService.querySpecParams(gid,cid,searching,generic);
        return ResponseEntity.ok(list);
    }

    /**
     * 编辑参数的分组信息
     * @param group
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> editGroup(SpecGroup group){
        this.specificationService.updateGroup(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("group")
    public ResponseEntity<Void> addGroup(SpecGroup group){
        this.specificationService.insertGroup(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除参数的分组信息
     * @param gid
     * @return
     */
    @DeleteMapping("group")
    public ResponseEntity<Void> deleteGroup(@RequestParam("gid") Long gid){
        this.specificationService.deleteGroup(gid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 添加新的规格参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> addParam(SpecParam specParam){
        this.specificationService.insertSpecParam(specParam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 编辑规格参数信息
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> editParam(SpecParam specParam){
        this.specificationService.updateSpecParam(specParam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除规格参数信息
     * @return
     */
    @DeleteMapping("param")
    public ResponseEntity<Void> deleteParam(@RequestParam("id") Long id){
        this.specificationService.deleteParam(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 通过分类id查询其之下的规格参数组，并封装进具体的规格参数specParams数据
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.querySpecsByCid(cid);
        if (BeanUtils.isNull(groups)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groups);
    }

}

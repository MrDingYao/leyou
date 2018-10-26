package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

public interface ISpecificationService {
    /**
     * 查询具体分类具有的规格信息
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecGroups(Long cid);

    /**
     * 查询具体分组下的规格参数集合
     * @param gid   : 所属分组group的id
     * @param cid   ：所属分类category的id
     * @param searching : 是否可查询
     * @param generic   ： 是否是通用规格参数
     * @return
     */
    List<SpecParam> querySpecParams(Long gid,Long cid,Boolean searching,Boolean generic);

    /**
     * 编辑分组名称
     * @param group
     */
    void updateGroup(SpecGroup group);

    /**
     * 删除分组
     * @param gid
     */
    void deleteGroup(Long gid);

    /**
     * 新增参数规格
     * @param specParam
     */
    void insertSpecParam(SpecParam specParam);

    /**
     * 编辑参数规格
     * @param specParam
     */
    void updateSpecParam(SpecParam specParam);

    /**
     * 删除参数规格
     * @param id
     */
    void deleteParam(Long id);

    /**
     * 新增分组
     * @param group
     */
    void insertGroup(SpecGroup group);

    /**
     * 通过分类id查询其之下的规格参数组，并封装进具体的规格参数specParams数据
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecsByCid(Long cid);
}

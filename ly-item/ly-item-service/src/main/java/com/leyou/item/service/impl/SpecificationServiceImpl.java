package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecificationServiceImpl implements ISpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 查询具体的分类具有的规格信息
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecGroups(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    /**
     * 查询具体分组下的规格参数集合
     * @param gid
     * @param cid
     * @param searching
     * @param generic
     * @return
     */
    @Override
    public List<SpecParam> querySpecParams(Long gid,Long cid,Boolean searching,Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return this.specParamMapper.select(specParam);
    }

    /**
     * 编辑分组名称
     * @param group
     */
    @Override
    public void updateGroup(SpecGroup group) {
        this.specGroupMapper.updateByPrimaryKeySelective(group);
    }

    /**
     * 新增分组
     * @param group
     */
    @Override
    public void insertGroup(SpecGroup group) {
        this.specGroupMapper.insert(group);
    }

    /**
     * 删除分组,添加事务
     * @param gid
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGroup(Long gid) {
        // 先删除分组表格tb_spec_group中的数据
        this.specGroupMapper.deleteByPrimaryKey(gid);
        // 然后删除该分组对应的规格参数数据,tb_spec_param
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        this.specParamMapper.delete(specParam);
    }

    /**
     * 新增参数规格
     * @param specParam
     */
    @Override
    public void insertSpecParam(SpecParam specParam) {
        this.specParamMapper.insert(specParam);
    }

    /**
     * 编辑参数规格
     * @param specParam
     */
    @Override
    public void updateSpecParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    /**
     * 删除参数规格
     * @param id
     */
    @Override
    public void deleteParam(Long id) {
        this.specParamMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过分类id查询其之下的规格参数组，并封装进具体的规格参数specParams数据
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        List<SpecGroup> groups = this.querySpecGroups(cid);
        groups.forEach(
                g -> g.setParams(this.querySpecParams(g.getId(),null,null,null))
        );
        return groups;
    }
}

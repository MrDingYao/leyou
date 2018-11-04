package com.leyou.item.service.impl;

import com.leyou.common.utils.JsonUtils;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.service.IGoodsService;
import com.leyou.item.service.ISpecificationService;
import com.leyou.item.service.ISpuDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpecificationServiceImpl implements ISpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Autowired
    private ISpuDetailService spuDetailService;

    @Autowired
    private IGoodsService goodsService;


    /**
     * 查询具体的分类具有的规格信息
     *
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
     *
     * @param gid
     * @param cid
     * @param searching
     * @param generic
     * @return
     */
    @Override
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return this.specParamMapper.select(specParam);
    }

    /**
     * 编辑分组名称
     *
     * @param group
     */
    @Override
    public void updateGroup(SpecGroup group) {
        this.specGroupMapper.updateByPrimaryKeySelective(group);
    }

    /**
     * 新增分组
     *
     * @param group
     */
    @Override
    public void insertGroup(SpecGroup group) {
        this.specGroupMapper.insert(group);
    }

    /**
     * 删除分组,添加事务
     *
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
        List<SpecParam> specParams = this.specParamMapper.select(specParam);
        if (!CollectionUtils.isEmpty(specParams)) {
            specParams.forEach(p -> this.deleteParam(p.getId()));
        }

    }

    /**
     * 新增参数规格
     *
     * @param specParam
     */
    @Override
    public void insertSpecParam(SpecParam specParam) {
        this.specParamMapper.insert(specParam);
    }

    /**
     * 编辑参数规格
     *
     * @param specParam
     */
    @Override
    public void updateSpecParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKeySelective(specParam);
        // 查询该规格参数所属的分类下的所有产品
        List<Spu> spus = this.goodsService.querySpuByCid(specParam.getCid());
        // 判断spus是否为空，不为空，则遍历集合发送rabbit MQ消息通知其它微服务更新
        if (!CollectionUtils.isEmpty(spus)) {
            spus.forEach(s -> this.goodsService.sendMessage(s.getId(),"update"));
        }
    }

    /**
     * 删除参数规格
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteParam(Long id) {
        SpecParam specParam = this.specParamMapper.selectByPrimaryKey(id);
        Long cid = specParam.getCid();
        // 查询该cid下的spu
        List<Spu> spus = this.goodsService.querySpuByCid(cid);
        // 如果有商品使用该规格参数，则修改每个商品的规格参数数据
        if (!CollectionUtils.isEmpty(spus)) {
            List<SpuDetail> spuDetails = spus.stream().map(spu -> this.spuDetailService.queryBySpuId(spu.getId()))
                    .collect(Collectors.toList());

            // 参数如果是通用参数
            if (specParam.getGeneric()) {
                // 修改拥有该参数的spu商品的genericSpec属性
                // 遍历，将通用参数属性中含有该规格参数的删除，然后更新

                for (SpuDetail s : spuDetails) {
                    String genericSpec = s.getGenericSpec();
                    Map<Long, Object> genericMap = JsonUtils.parseMap(genericSpec, Long.class, Object.class);
                    Set<Long> keys = genericMap.keySet();
                    if (keys.contains(id)) {
                        genericMap.remove(id);
                        s.setGenericSpec(JsonUtils.serialize(genericMap));
                        this.spuDetailService.updateSpuDetail(s);
                        break;
                    }
                }
            } else {
                // 如果是特有参数,需要修改拥有该参数的spu和sku
                // 修改商品详情数据
                Map<Long, Object> specialMap = new HashMap<>();
                for (SpuDetail spuDetail : spuDetails) {
                    String specialSpec = spuDetail.getSpecialSpec();
                    specialMap = JsonUtils.parseMap(specialSpec, Long.class, Object.class);
                    if (specialMap.keySet().contains(id)) {
                        specialMap.remove(id);
                        spuDetail.setSpecialSpec(JsonUtils.serialize(specialMap));
                        this.spuDetailService.updateSpuDetail(spuDetail);
                    }
                    // 修改sku数据
                    List<Sku> skuList = this.goodsService.querySkusById(spuDetail.getSpuId());
                    for (Sku sku : skuList) {
                        String ownSpec = sku.getOwnSpec();
                        Map<Long, Object> ownSpecMap = JsonUtils.parseMap(ownSpec, Long.class, Object.class);
                        if (ownSpecMap.keySet().contains(id)) {
                            // 移除sku中的ownSpec属性中的被删除的规格参数
                            ownSpecMap.remove(id);
                            sku.setOwnSpec(JsonUtils.serialize(ownSpecMap));
                            // 修改indexes属性
                            List<String> index = new ArrayList<>();
                            // 获得所有的key
                            Set<Long> keySet = specialMap.keySet();
                            for (Long key : keySet) {
                                // 取出所有的选择的数组
                                List<String> chooses = (List<String>) specialMap.get(key);
                                // 取出该sku对应的值
                                String value = (String) ownSpecMap.get(key);
                                // 查出该值在数组中的索引位
                                int i = chooses.indexOf(value);
                                index.add(i + "");
                            }
                            // 最后修改sku的真实的indexes属性
                            String indexes = StringUtils.join(index, "_");
                            sku.setIndexes(indexes);
                            this.goodsService.updateSku(sku);
                        }
                    }
                }

            }

            // 最后发送rabbitMQ消息，通知其它微服务更新
            spus.forEach(s -> this.goodsService.sendMessage(s.getId(),"update"));
        }

        // 删除规格参数数据
        this.specParamMapper.deleteByPrimaryKey(id);

    }

    /**
     * 通过分类id查询其之下的规格参数组，并封装进具体的规格参数specParams数据
     *
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        List<SpecGroup> groups = this.querySpecGroups(cid);
        groups.forEach(
                g -> g.setParams(this.querySpecParams(g.getId(), null, null, null))
        );
        return groups;
    }
}

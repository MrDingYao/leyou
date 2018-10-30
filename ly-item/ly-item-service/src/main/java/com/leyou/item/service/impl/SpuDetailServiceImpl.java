package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.ISpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 26 22:23
 **/
@Service
public class SpuDetailServiceImpl implements ISpuDetailService {

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    /**
     * 搜索所有的商品详情
     * @return
     */
    @Override
    public List<SpuDetail> queryAll() {
        return this.spuDetailMapper.selectAll();
    }

    /**
     * 通过souId查询对应的详情
     * @param id
     * @return
     */
    @Override
    public SpuDetail queryBySpuId(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改商品详情
     * @param s
     */
    @Override
    public void updateSpuDetail(SpuDetail s) {
        this.spuDetailMapper.updateByPrimaryKeySelective(s);
    }
}

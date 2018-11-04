package com.leyou.user.service.impl;

import com.leyou.auth.entity.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Address;
import com.leyou.item.pojo.ProvinceCityDown;
import com.leyou.user.filter.LoginInterceptor;
import com.leyou.user.mapper.AddressMapper;
import com.leyou.user.mapper.ProvinceCityDownMapper;
import com.leyou.user.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 30 16:45
 **/
@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProvinceCityDownMapper provinceCityDownMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用户查询地址
     * @return
     */
    @Override
    public List<Address> queryAddressByUid() {
        UserInfo user = LoginInterceptor.getLoginUser();
        Address address = new Address();
        address.setUserId(user.getId());
        return this.addressMapper.select(address);
    }

    /**
     * 新增地址
     * @param address
     */
    @Override
    public Boolean addAddress(Address address) {
        address.setUserId(LoginInterceptor.getLoginUser().getId());
        return this.addressMapper.insertSelective(address) == 1;
    }

    /**
     * 编辑地址
     * @param address
     * @return
     */
    @Override
    public Boolean editAddress(Address address) {
        address.setUserId(LoginInterceptor.getLoginUser().getId());
        return this.addressMapper.updateByPrimaryKeySelective(address) == 1;
    }

    /**
     * 查询所有的省市区
     * @return
     */
    @Override
    public List<ProvinceCityDown> queryAllArea() {
        List<ProvinceCityDown> list = new ArrayList<>();
        // 先从redis中查,
        if (this.redisTemplate.hasKey("cities")) {
            String cities = this.redisTemplate.opsForValue().get("cities");
            list = JsonUtils.parseList(cities, ProvinceCityDown.class);
        } else {
            list = this.provinceCityDownMapper.selectAll();
            this.redisTemplate.opsForValue().set("cities", Objects.requireNonNull(JsonUtils.serialize(list)));
        }

        return list;
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @Override
    public Boolean deleteAddress(Long id) {
        return this.addressMapper.deleteByPrimaryKey(id) == 1;
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setDefault(Long id) {
        // 将该用户的所有地址设置为非default
        Address address = new Address();
        Long userId = LoginInterceptor.getLoginUser().getId();
        this.addressMapper.setNotDefault(userId);
        // 将当前这个id的地址设置为default
        address.setId(id);
        address.setIsDefault(true);
        this.addressMapper.updateByPrimaryKeySelective(address);
    }

    /**
     * 根据地址id查询地址
     * @param id
     * @return
     */
    @Override
    public Address queryAddressById(Long id) {
        return this.addressMapper.selectByPrimaryKey(id);
    }
}

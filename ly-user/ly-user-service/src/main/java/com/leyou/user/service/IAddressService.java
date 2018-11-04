package com.leyou.user.service;

import com.leyou.item.pojo.Address;
import com.leyou.item.pojo.ProvinceCityDown;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 30 16:14
 **/
public interface IAddressService {

    /**
     * 用户查询地址
     * @return
     */
    List<Address> queryAddressByUid();

    /**
     * 新增地址
     * @param address
     */
    Boolean addAddress(Address address);

    /**
     * 编辑地址
     * @param address
     * @return
     */
    Boolean editAddress(Address address);

    /**
     * 查询所有的省市区
     * @return
     */
    List<ProvinceCityDown> queryAllArea();

    /**
     * 删除地址
     * @param id
     * @return
     */
    Boolean deleteAddress(Long id);

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    void setDefault(Long id);

    /**
     * 根据地址id查询地址
     * @param id
     * @return
     */
    Address queryAddressById(Long id);
}

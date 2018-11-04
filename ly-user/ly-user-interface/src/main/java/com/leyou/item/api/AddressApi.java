package com.leyou.item.api;

import com.leyou.item.pojo.Address;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 11 - 03 10:56
 **/
@RequestMapping("address")
public interface AddressApi {

    /**
     * 通过用户选择的地址id查询具体的地址
     * @return
     */
    @GetMapping("{id}")
    Address queryAddressById(@PathVariable("id") Long id);
}

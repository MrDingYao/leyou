package com.leyou.user.controller;

import com.leyou.item.pojo.Address;
import com.leyou.item.pojo.ProvinceCityDown;
import com.leyou.user.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 30 16:13
 **/
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    /**
     * 查询用户保存的地址信息
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Address>> queryAddressByUid(){
        List<Address> addressList = this.addressService.queryAddressByUid();
        if (CollectionUtils.isEmpty(addressList)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(addressList);
    }

    /**
     * 新增地址
     * @param address
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addAddress(@RequestBody Address address){
        Boolean result = this.addressService.addAddress(address);
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 编辑地址
     * @param address
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> editAddress(@RequestBody Address address){
        Boolean result = this.addressService.editAddress(address);
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 查询所有省市区
     * @return
     */
    @GetMapping("area")
    public ResponseEntity<List<ProvinceCityDown>> querySonArea(){
        List<ProvinceCityDown> provinceCityDowns = this.addressService.queryAllArea();
        if (CollectionUtils.isEmpty(provinceCityDowns)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(provinceCityDowns);
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") Long id){
        Boolean result = this.addressService.deleteAddress(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @PutMapping("{id}")
    public ResponseEntity<Void> setDefault(@PathVariable("id") Long id){
        this.addressService.setDefault(id);
        return ResponseEntity.ok().build();

    }

    /**
     * 通过用户选择的地址id查询具体的地址
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Address> queryAddressById(@PathVariable("id") Long id){
        Address address = this.addressService.queryAddressById(id);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(address);
    }

}

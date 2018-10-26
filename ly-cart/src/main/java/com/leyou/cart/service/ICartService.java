package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 23 19:42
 **/
public interface ICartService {
    /**
     * 添加商品至购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询用户的购物车中的商品
     * @return
     */
    List<Cart> queryCartList();

    /**
     * 修改购物车中的商品数量
     * @param skuId
     * @param num
     */
    void editCart(Long skuId, Integer num);

    /**
     * 删除购物车中的商品
     * @param skuId
     */
    void deleteCart(Long skuId);
}

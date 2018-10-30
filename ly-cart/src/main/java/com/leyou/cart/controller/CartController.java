package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 23 19:41
 **/
@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 添加商品至购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 添加本地保存的购物车信息集合至redis
     * @param cartList
     * @return
     */
    @PostMapping("addLocal")
    public ResponseEntity<Void> addCartList(@RequestBody List<Cart> cartList){
        cartList.forEach(c -> this.cartService.addCart(c));
        return ResponseEntity.ok().build();
    }

    /**
     * 获得购物车中商品
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList(){
        List<Cart> carts = this.cartService.queryCartList();
        if (carts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车中的商品数量
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> editCart(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        this.cartService.editCart(skuId, num);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除购物车中的商品
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id")Long id){
        this.cartService.deleteCart(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 点击提交订单后，删除购物车中的商品
     * @param ids
     * @return
     */
    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteCarts(@RequestParam("ids") String ids){
        this.cartService.deleteCarts(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询用户购物车中商品的数量
     * @return
     */
    @GetMapping("count")
    public ResponseEntity<Integer> queryCartCount(){
        Integer count = this.cartService.queryCartCount();
        return ResponseEntity.ok(count);
    }

}
